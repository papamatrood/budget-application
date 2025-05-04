import { DatePipe } from "@angular/common";
import { Component } from "@angular/core";

@Component({
  selector: "jhi-footer",
  templateUrl: "./footer.component.html",
  imports: [DatePipe],
})
export default class FooterComponent {
  date = new Date();
}
