import { Component, OnDestroy, OnInit, inject, signal } from "@angular/core";
import { Router, RouterModule } from "@angular/router";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";

import SharedModule from "app/shared/shared.module";
import { AccountService } from "app/core/auth/account.service";
import { Account } from "app/core/auth/account.model";
import jsPDF from "jspdf";
import autoTable from 'jspdf-autotable';

@Component({
  selector: "jhi-home",
  templateUrl: "./home.component.html",
  styleUrl: "./home.component.scss",
  imports: [SharedModule, RouterModule],
})
export default class HomeComponent {


  generateBonAchat(): void {
    const doc = new jsPDF();

    const marginRight = 150;
    const marginLeft = 10;
    const margin = 100;

    // Titre
    doc.setFont("helvetica", "bold");
    doc.setFontSize(10);
    doc.text("MINISTERE DE L’ENTREPRENARIAT NATIONAL DE", 10, 10);
    doc.text("L’EMPLOI ET DE LA FORMATION PROFESSIONNELLE", 10, 15);
    doc.text("AGENCE POUR LA PROMOTION DE L’EMPLOI DES JEUNES", 10, 25);
    doc.text("SECTION: 325", 10, 30);
    doc.text("Nature 60-1-1-1-6 Produits alimentaires", 10, 35);
    doc.text("CHAPITRE 50-1-2003-0031-001-000000", 10, 40);

    doc.setFont('helvetica', 'bold').setFontSize(10);
    doc.text('REPUBLIQUE DU MALI', marginRight, 10);
    doc.setFont('helvetica', 'italic', 'bold').setFontSize(10);
    doc.text('Un Peuple - Un But - Une Foi', marginRight, 15);


    // Dessiner le rectangle
    doc.setDrawColor(0, 0, 0); // Couleur du contour
    doc.setFillColor(192, 192, 192); // Gris clair
    doc.setLineWidth(0.8);
    doc.rect(marginLeft, 45, 190, 8, 'FD');

    doc.setFontSize(12);
    const pageWidth = doc.internal.pageSize.getWidth();
    const textWidth = doc.getTextWidth("BON D’ACHAT N°24-001");
    doc.text("BON D’ACHAT N°24-001", (pageWidth - textWidth) / 2, 50);



    // Informations du fournisseur
    doc.setFontSize(16);
    doc.text("SPEED SERVICES", 10, 60);
    doc.setFontSize(10);
    // doc.setFont("normal");
    doc.setFont('helvetica', 'normal', 'bold').setFontSize(10);

    doc.text("Adresse : Magnambougou près de la station shell", 10, 65);
    doc.text("Tél : 95 02 02 09", 10, 70);
    doc.text("NIF : 086148324 V", 10, 75);
    doc.text("RC : MA.BKO.2020.A.124", 10, 80);
    doc.text("Priere de bien vouloir assurer a l'Agence pour la Promotion de l'Emploi des Jeunes (APEJ) les services ci-dessous", 10, 85, { maxWidth: 190 });

    // Tableau
    const tableColumn = ["DÉSIGNATION", "QUANTITÉ", "PRIX UNITAIRE", "MONTANTS"];
    const tableRows = [
      ["Lait en poudre boite moyenne NIDO (carton de 12)", "30", "130 000", "3 900 000"],
      ["Café Nespresso (paquet)", "32", "5 500", "176 000"],
      ["Sucre en morceau (Carton)", "41", "25 000", "1 025 000"],
      ["Sac de sucre en poudre (sac de 50 kg)", "170", "38 000", "6 460 000"],
      ["Eau minérale petite bouteille 33 cl (carton 24)", "186", "1 000", "186 000"],
      ["Lipton (carton)", "22", "12 000", "264 000"],
      ["Jus (Carton de 24)", "22", "12 000", "264 000"],

    ];

    autoTable(doc, {
      startY: 95,
      head: [tableColumn],
      body: tableRows,
      theme: 'grid',
      headStyles: { fillColor: [255, 255, 255], textColor: [0, 0, 0], halign: 'center', lineColor: [0, 0, 0], lineWidth: 0.5, },
      styles: { fillColor: [255, 255, 255], textColor: [0, 0, 0], lineColor: [0, 0, 0], lineWidth: 0.5 },
      bodyStyles: {
        lineColor: [0, 0, 0],
        lineWidth: 0.5,
        halign: 'right'
      },
      columnStyles: {
        0: { halign: 'left' },
      },

    });

    let yPosition = (doc as any).lastAutoTable.finalY + 5;


    doc.setDrawColor(0, 0, 0); // Couleur du contour
    doc.setFillColor(192, 192, 192); // Gris clair
    doc.setLineWidth(0.5);
    doc.rect(marginLeft + 88, yPosition - 5, 98, 23, 'FD');

    doc.setDrawColor(0, 0, 0).setLineWidth(0.5);
    doc.line(169, yPosition - 5, 169, yPosition + 18);

    doc.setFont('normal')
    // Totaux
    doc.text(`Total HT`, margin, yPosition);
    doc.text(`TVA 18%`, margin, yPosition + 5);
    doc.text(`Montant à précompter 40% de la TVA`, margin, yPosition + 10);
    doc.text(`Total TTC`, margin, yPosition + 15);

    // Montant en chiffres
    let THT = 12691000;
    let TVA = (THT * 18) / 100
    let TVA40 = (TVA * 40) / 100
    let TTC = THT + TVA
    doc.text('' + THT, pageWidth - marginLeft - 6, yPosition, { align: "right" });
    doc.text('' + TVA, pageWidth - marginLeft - 6, yPosition + 5, { align: "right" });
    doc.text('' + TVA40, pageWidth - marginLeft - 6, yPosition + 10, { align: "right" });
    doc.text('' + TTC, pageWidth - marginLeft - 6, yPosition + 15, { align: "right" });

    // Montant en lettres
    doc.setFont('normal', 'bold').setFontSize(12);
    doc.text("Arrêté ce bon à la somme de :", margin, yPosition + 30);
    doc.setFont('normal', 'normal').setFontSize(11);


    let text = "Quatorze millions neuf cent soixante-quinze mille trois cent quatre-vingts Francs CFA";
    let textX = pageWidth - marginLeft - 6;
    doc.text(text, textX, yPosition + 37, { align: "right" });
    doc.rect(50, yPosition + 32, 145, 8)
    // doc.rect(textX - textWidth + 1 , yPosition + 32, textWidth + 150, 8);



    // Signatures
    doc.text("LE CONTROLEUR FINANCIER", 10, yPosition + 50);
    doc.text("LE DIRECTEUR GENERAL", 140, yPosition + 50);
    doc.text("Mamadou BA", 140, yPosition + 55);

    doc.save("bon_achat.pdf");
  }












  // account = signal<Account | null>(null);

  // private readonly destroy$ = new Subject<void>();

  // private readonly accountService = inject(AccountService);
  // private readonly router = inject(Router);

  // ngOnInit(): void {
  //   this.accountService
  //     .getAuthenticationState()
  //     .pipe(takeUntil(this.destroy$))
  //     .subscribe((account) => this.account.set(account));
  // }

  // login(): void {
  //   this.router.navigate(["/login"]);
  // }

  // ngOnDestroy(): void {
  //   this.destroy$.next();
  //   this.destroy$.complete();
  // }
}
