import { Component, inject } from "@angular/core";
import { Router, RouterModule } from "@angular/router";
import { AccountService } from "app/core/auth/account.service";
import { LoginService } from "app/login/login.service";
import SharedModule from "app/shared/shared.module";

@Component({
  selector: "jhi-header",
  imports: [RouterModule, SharedModule],
  templateUrl: "./header.component.html",
  styleUrl: "./header.component.scss",
})
export class HeaderComponent {
  account = inject(AccountService).trackCurrentAccount();

  private readonly loginService = inject(LoginService);
  private readonly router = inject(Router);

  login(): void {
    this.router.navigate(["/login"]);
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate([""]);
  }
}
