import { Injectable } from '@angular/core';
import { HousingLocation } from './housing-location';
import { Apply } from './apply';

@Injectable({
  providedIn: 'root'
})
export class HousingService {
  allUrl = 'http://localhost:8080/house/all';
  oneUrl = 'http://localhost:8080/house/one';
  applyUrl = 'http://localhost:8080/apply';
  lastApplyUrl = 'http://localhost:8080/apply/last';
  constructor() { }

  async getAll(): Promise<HousingLocation[]> {
    const data = await fetch(this.allUrl);
    return await data.json() ?? [];
  }

  async getOne(id: Number): Promise<HousingLocation | undefined> {
    const data = await fetch(`${this.oneUrl}?id=${id}`);
    return await data.json() ?? {};
  }

  async submit(firstName: string, lastName: string, email: string, date: Date, id: number) {
    let data = new FormData();
    data.append("first-name", firstName);
    data.append("last-name", lastName);
    data.append("email", email);
    data.append("date", date.toISOString());
    await fetch(
      `${this.applyUrl}?id=${id}`,
      {
        method: "POST",
        body: data,
      }
    );
  }

  async getLastApply(id: Number): Promise<Apply | undefined> {
    const data = await fetch(`${this.lastApplyUrl}?id=${id}`);
    return await data.json() ?? {};
  }
}
