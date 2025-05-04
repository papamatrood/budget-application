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

import { sampleWithRequiredData } from "../engagement.test-samples";
import { EngagementService } from "../service/engagement.service";

import { EngagementComponent } from "./engagement.component";
import SpyInstance = jest.SpyInstance;

describe("Engagement Management Component", () => {
  let comp: EngagementComponent;
  let fixture: ComponentFixture<EngagementComponent>;
  let service: EngagementService;
  let routerNavigateSpy: SpyInstance<Promise<boolean>>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EngagementComponent],
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
      .overrideTemplate(EngagementComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(EngagementComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EngagementService);
    routerNavigateSpy = jest.spyOn(comp.router, "navigate");

    jest
      .spyOn(service, "query")
      .mockReturnValueOnce(
        of(
          new HttpResponse({
            body: [{ id: 24171 }],
            headers: new HttpHeaders({
              link: '<http://localhost/api/foo?page=1&size=20>; rel="next"',
            }),
          }),
        ),
      )
      .mockReturnValueOnce(
        of(
          new HttpResponse({
            body: [{ id: 18750 }],
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
    expect(comp.engagements()[0]).toEqual(
      expect.objectContaining({ id: 24171 }),
    );
  });

  describe("trackId", () => {
    it("should forward to engagementService", () => {
      const entity = { id: 24171 };
      jest.spyOn(service, "getEngagementIdentifier");
      const id = comp.trackId(entity);
      expect(service.getEngagementIdentifier).toHaveBeenCalledWith(entity);
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
