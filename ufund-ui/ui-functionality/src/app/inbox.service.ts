import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Message } from './message';
@Injectable({
  providedIn: 'root'
})
export class InboxService {

  private messageUrl = 'http://localhost:8080/message'; // URL to web API

    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    constructor(
        private http: HttpClient,
        private messageService: MessageService) { }

    getMessages(username: string, password: string): Observable<Message[]> {
      const url = `${this.messageUrl}?username=${username}&password=${password}`;
      let something = this.http.get<Message[]>(url).pipe(
        catchError(this.handleError<Message[]>('getAllMessages')))
        return something;
    }

    createMessage(username: string, password: string, message: Message): Observable<Message> {
      console.log(`${message.username}`)
      const url = `${this.messageUrl}?username=${username}&password=${password}`;
      let something = this.http.put<Message>(url,message).pipe(
        catchError(this.handleError<Message>('createMessage')))
        return something;
    }

    deleteMessage(username: string, password: string, id: number): Observable<Message> {
      const url = `${this.messageUrl}?username=${username}&password=${password}&id=${id}`;
      let something = this.http.delete<Message>(url).pipe(
        catchError(this.handleError<Message>('deleteMessage')))
        return something;
    }

    /** Log a HeroService message with the MessageService */
    private log(message: string) {
      this.messageService.add(`InboxService: ${message}`);
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
