import { EventEmitter, Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { AuthCredentials } from './login';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class LoginService {
  newLogoutEvent: EventEmitter<void> = new EventEmitter<void>();
  private authUrl = 'http://localhost:8080/auth'; // URL to web API

    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    constructor(
        private http: HttpClient,
        private messageService: MessageService) { }

    authenticate(username: string, password: string): Observable<any> {
        return this.http.get<AuthCredentials>(`${this.authUrl}?username=${username}&password=${password}`);
    }
    
    emitNewLogoutEvent(){
      this.newLogoutEvent.emit();
    }
    
      // Function to get permission level of a user
      getPermissionLevel(username: string, password: string): Observable<string> {
        const url = `${this.authUrl}?username=${username}&password=${password}`;
        let something = this.http.get<string>(url).pipe(
          catchError(this.handleError<string>('getPermissionLevel'))
        )
        console.log(`test: ${something}`)
        return something;

      }
      

    /** Log a HeroService message with the MessageService */
    private log(message: string) {
      this.messageService.add(`LoginService: ${message}`);
  }

  /**
  * Handle Http operation that failed.
  * Let the app continue.
  *
  * @param operation - name of the operation that failed
  * @param result - optional value to return as the observable result
  */
  private handleError<T>(operation = 'operation', result?: T) {
      return (error: any): Observable<T> => {
          console.error(error);
          this.log(`${operation} failed: ${error.message}`);
          return of(result as T);
      };
  }
}
