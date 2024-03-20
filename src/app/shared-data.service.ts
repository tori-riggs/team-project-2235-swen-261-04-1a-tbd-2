import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SharedDataService {
  private username: string = '';
  private permissionLevel: string = '';

  constructor() { }

  setUsername(username: string): void {
    this.username = username;
  }

  getUsername(): string {
    return this.username;
  }

  setPermissionLevel(permissionLevel: string): void {
    this.permissionLevel = permissionLevel;
  }

  getPermissionLevel(): string {
    return this.permissionLevel;
  }
}
