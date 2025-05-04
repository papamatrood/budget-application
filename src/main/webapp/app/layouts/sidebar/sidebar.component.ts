import { isPlatformBrowser, NgClass } from "@angular/common";
import { Component, Inject, PLATFORM_ID } from "@angular/core";
import { RouterLink } from "@angular/router";

@Component({
  selector: "jhi-sidebar",
  imports: [NgClass, RouterLink],
  templateUrl: "./sidebar.component.html",
  styleUrl: "./sidebar.component.scss",
})
export class SidebarComponent {
  selectedTab: string | null = "Tableau de bord";

  constructor(@Inject(PLATFORM_ID) private platformId: object) {
    if (isPlatformBrowser(this.platformId)) {
      this.selectedTab =
        localStorage.getItem("selectedTab") ?? "Tableau de bord";
    }
  }

  selectTab(tab: string, collapseId: string): void {
    if (this.selectedTab === tab) {
      this.selectedTab = null;
      if (isPlatformBrowser(this.platformId)) {
        localStorage.removeItem("selectedTab");
      }
    } else {
      this.selectedTab = tab;
      if (isPlatformBrowser(this.platformId)) {
        localStorage.setItem("selectedTab", tab);
      }
    }

    // Fermer tous les autres onglets
    const collapses = document.querySelectorAll(".collapse");
    collapses.forEach((collapse: any) => {
      if (collapse.id !== collapseId) {
        collapse.classList.remove("show"); // Ferme les autres
      }
    });
  }
}
