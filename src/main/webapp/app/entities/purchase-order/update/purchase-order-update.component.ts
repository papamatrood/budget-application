import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAnnexDecision } from 'app/entities/annex-decision/annex-decision.model';
import { AnnexDecisionService } from 'app/entities/annex-decision/service/annex-decision.service';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { SupplierService } from 'app/entities/supplier/service/supplier.service';
import { IEngagement } from 'app/entities/engagement/engagement.model';
import { EngagementService } from 'app/entities/engagement/service/engagement.service';
import { PurchaseOrderService } from '../service/purchase-order.service';
import { IPurchaseOrder } from '../purchase-order.model';
import { PurchaseOrderFormGroup, PurchaseOrderFormService } from './purchase-order-form.service';

@Component({
  selector: 'jhi-purchase-order-update',
  templateUrl: './purchase-order-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PurchaseOrderUpdateComponent implements OnInit {
  isSaving = false;
  purchaseOrder: IPurchaseOrder | null = null;

  annexDecisionsSharedCollection: IAnnexDecision[] = [];
  suppliersSharedCollection: ISupplier[] = [];
  engagementsSharedCollection: IEngagement[] = [];

  protected purchaseOrderService = inject(PurchaseOrderService);
  protected purchaseOrderFormService = inject(PurchaseOrderFormService);
  protected annexDecisionService = inject(AnnexDecisionService);
  protected supplierService = inject(SupplierService);
  protected engagementService = inject(EngagementService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PurchaseOrderFormGroup = this.purchaseOrderFormService.createPurchaseOrderFormGroup();

  compareAnnexDecision = (o1: IAnnexDecision | null, o2: IAnnexDecision | null): boolean =>
    this.annexDecisionService.compareAnnexDecision(o1, o2);

  compareSupplier = (o1: ISupplier | null, o2: ISupplier | null): boolean => this.supplierService.compareSupplier(o1, o2);

  compareEngagement = (o1: IEngagement | null, o2: IEngagement | null): boolean => this.engagementService.compareEngagement(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseOrder }) => {
      this.purchaseOrder = purchaseOrder;
      if (purchaseOrder) {
        this.updateForm(purchaseOrder);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const purchaseOrder = this.purchaseOrderFormService.getPurchaseOrder(this.editForm);
    if (purchaseOrder.id !== null) {
      this.subscribeToSaveResponse(this.purchaseOrderService.update(purchaseOrder));
    } else {
      this.subscribeToSaveResponse(this.purchaseOrderService.create(purchaseOrder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseOrder>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(purchaseOrder: IPurchaseOrder): void {
    this.purchaseOrder = purchaseOrder;
    this.purchaseOrderFormService.resetForm(this.editForm, purchaseOrder);

    this.annexDecisionsSharedCollection = this.annexDecisionService.addAnnexDecisionToCollectionIfMissing<IAnnexDecision>(
      this.annexDecisionsSharedCollection,
      purchaseOrder.annexDecision,
    );
    this.suppliersSharedCollection = this.supplierService.addSupplierToCollectionIfMissing<ISupplier>(
      this.suppliersSharedCollection,
      purchaseOrder.supplier,
    );
    this.engagementsSharedCollection = this.engagementService.addEngagementToCollectionIfMissing<IEngagement>(
      this.engagementsSharedCollection,
      purchaseOrder.engagement,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.annexDecisionService
      .query()
      .pipe(map((res: HttpResponse<IAnnexDecision[]>) => res.body ?? []))
      .pipe(
        map((annexDecisions: IAnnexDecision[]) =>
          this.annexDecisionService.addAnnexDecisionToCollectionIfMissing<IAnnexDecision>(
            annexDecisions,
            this.purchaseOrder?.annexDecision,
          ),
        ),
      )
      .subscribe((annexDecisions: IAnnexDecision[]) => (this.annexDecisionsSharedCollection = annexDecisions));

    this.supplierService
      .query()
      .pipe(map((res: HttpResponse<ISupplier[]>) => res.body ?? []))
      .pipe(
        map((suppliers: ISupplier[]) =>
          this.supplierService.addSupplierToCollectionIfMissing<ISupplier>(suppliers, this.purchaseOrder?.supplier),
        ),
      )
      .subscribe((suppliers: ISupplier[]) => (this.suppliersSharedCollection = suppliers));

    this.engagementService
      .query()
      .pipe(map((res: HttpResponse<IEngagement[]>) => res.body ?? []))
      .pipe(
        map((engagements: IEngagement[]) =>
          this.engagementService.addEngagementToCollectionIfMissing<IEngagement>(engagements, this.purchaseOrder?.engagement),
        ),
      )
      .subscribe((engagements: IEngagement[]) => (this.engagementsSharedCollection = engagements));
  }
}
