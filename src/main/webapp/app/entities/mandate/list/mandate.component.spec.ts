import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  inject,
  tick,
} from "@angular/core/testing";
import {
  HttpHeaders,
  HttpResponse,
  provideHttpClient,
} from "@angular/common/http";
import { ActivatedRoute } from "@angular/router";
import { Subject, of } from "rxjs";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

import { sampleWithRequiredData } from "../mandate.test-samples";
import { MandateService } from "../service/mandate.service";

import { MandateComponent } from "./mandate.component";
import SpyInstance = jest.SpyInstance;

describe("Mandate Management Component", () => {
  let comp: MandateComponent;
  let fixture: ComponentFixture<MandateComponent>;
  let service: MandateService;
  let routerNavigateSpy: SpyInstance<Promise<boolean>>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MandateComponent],
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: "id,asc",
            }),
            queryParamMap: of(
              jest.requireActual("@angular/router").convertToParamMap({
                page: "1",
                size: "1",
                sort: "id,desc",
                "filter[someId.in]": "dc4279ea-cfb9-11ec-9d64-0242ac120002",
              }),
            ),
            snapshot: {
              queryParams: {},
              queryParamMap: jest
                .requireActual("@angular/router")
                .convertToParamMap({
                  page: "1",
                  size: "1",
                  sort: "id,desc",
                  "filter[someId.in]": "dc4279ea-cfb9-11ec-9d64-0242ac120002",
                }),
            },
          },
        },
      ],
    })
      .overrideTemplate(MandateComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(MandateComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MandateService);
    routerNavigateSpy = jest.spyOn(comp.router, "navigate");

    jest
      .spyOn(service, "query")
      .mockReturnValueOnce(
        of(
          new HttpResponse({
            body: [{ id: 5755 }],
            headers: new HttpHeaders({
              link: '<http://localhost/api/foo?page=1&size=20>; rel="next"',
            }),
          }),
        ),
      )
      .mockReturnValueOnce(
        of(
          new HttpResponse({
            body: [{ id: 18821 }],
            headers: new HttpHeaders({
              link: '<http://localhost/api/foo?page=0&size=20>; rel="prev",<http://localhost/api/foo?page=2&size=20>; rel="next"',
            }),
          }),
        ),
      );
  });

  it("should call load all on init", () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.mandates()[0]).toEqual(expect.objectContaining({ id: 5755 }));
  });

  describe("trackId", () => {
    it("should forward to mandateService", () => {
      const entity = { id: 5755 };
      jest.spyOn(service, "getMandateIdentifier");
      const id = comp.trackId(entity);
      expect(service.getMandateIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });

  it("should calculate the sort attribute for a non-id attribute", () => {
    // WHEN
    comp.navigateToWithComponentValues({
      predicate: "non-existing-column",
      order: "asc",
    });

    // THEN
    expect(routerNavigateSpy).toHaveBeenLastCalledWith(
      expect.anything(),
      expect.objectContaining({
        queryParams: expect.objectContaining({
          sort: ["non-existing-column,asc"],
        }),
      }),
    );
  });

  it("should load a page", () => {
    // WHEN
    comp.navigateToPage(1);

    // THEN
    expect(routerNavigateSpy).toHaveBeenCalled();
  });

  it("should calculate the sort attribute for an id", () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(
      expect.objectContaining({ sort: ["id,desc"] }),
    );
  });

  it("should calculate the filter attribute", () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(
      expect.objectContaining({
        "someId.in": ["dc4279ea-cfb9-11ec-9d64-0242ac120002"],
      }),
    );
  });

  describe("delete", () => {
    let ngbModal: NgbModal;
    let deleteModalMock: any;

    beforeEach(() => {
      deleteModalMock = { componentInstance: {}, closed: new Subject() };
      // NgbModal is not a singleton using TestBed.inject.
      // ngbModal = TestBed.inject(NgbModal);
      ngbModal = (comp as any).modalService;
      jest.spyOn(ngbModal, "open").mockReturnValue(deleteModalMock);
    });

    it("on confirm should call load", inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(comp, "load");

        // WHEN
        comp.delete(sampleWithRequiredData);
        deleteModalMock.closed.next("deleted");
        tick();

        // THEN
        expect(ngbModal.open).toHaveBeenCalled();
        expect(comp.load).toHaveBeenCalled();
      }),
    ));

    it("on dismiss should call load", inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(comp, "load");

        // WHEN
        comp.delete(sampleWithRequiredData);
        deleteModalMock.closed.next();
        tick();

        // THEN
        expect(ngbModal.open).toHaveBeenCalled();
        expect(comp.load).not.toHaveBeenCalled();
      }),
    ));
  });
});
