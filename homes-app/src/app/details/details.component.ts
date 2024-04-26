import { Component, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { HousingService } from '../housing.service';
import { HousingLocation } from '../housing-location';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Apply } from '../apply';

@Component({
  selector: 'app-details',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent {
  route: ActivatedRoute = inject(ActivatedRoute);
  housingLocationId: number = 0;
  housingService: HousingService = inject(HousingService);
  housingLocation: HousingLocation | undefined;
  applyForm = new FormGroup(
    {
      firstName: new FormControl('', Validators.required),
      lastName: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
    }
  );
  lastApply: Apply | undefined;
  failed: boolean = false;

  constructor() {
    this.housingLocationId = Number(this.route.snapshot.params['id']);
    this.housingService.getOne(this.housingLocationId).then(
      (housingLocation: HousingLocation | undefined) => {
        this.housingLocation = housingLocation;
      }
    ).catch(
      (reason: any) => {
        this.failed = true;
      }
    );
    this.housingService.getLastApply(this.housingLocationId).then(
      (apply: Apply | undefined) => {
        this.lastApply = apply;
      }
    ).catch(
      (reason: any) => {
        this.lastApply = undefined;
      }
    );
  }
  submitApplication() {
    this.housingService.submit(
      this.applyForm.value.firstName ?? '',
      this.applyForm.value.lastName ?? '',
      this.applyForm.value.email ?? '',
      new Date(),
      this.housingLocationId
    );
  }
}
