import { EventEmitter, Injectable } from '@angular/core';
import { Need } from './need';
import { NEEDS } from './mock-needs';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { NeedCheckout } from './needs-checkout';

@Injectable({
  providedIn: 'root'
})
export class NeedsCheckoutService {
  addToCartEvent: EventEmitter<void> = new EventEmitter<void>();
  private needsCheckoutUrl = 'http://localhost:8080/needs/funding-basket'

    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    constructor(private http: HttpClient, private messageService: MessageService) { }

  getFundingBasket(username: string, password: string): Observable<NeedCheckout> {
    return this.http.get<NeedCheckout>(`${this.needsCheckoutUrl}?username=${username}&password=${password}`)
    .pipe(catchError(this.handleError<NeedCheckout>('getFundingBasket')));
  }

  addNeedToFundingBasket(username: string, password: string, id: number, quantity: number): Observable<NeedCheckout> {
    return this.http.post<NeedCheckout>(`${this.needsCheckoutUrl}?id=${id}&quantity=${quantity}&username=${username}&password=${password}`,quantity)
    .pipe(catchError(this.handleError<NeedCheckout>('addNeedToFundingBasket')));
  }

  removeNeedFromFundingBasket(username: string, id: number, password: string): Observable<NeedCheckout> {
    return this.http.delete<NeedCheckout>(`${this.needsCheckoutUrl}?id=${id}&username=${username}&password=${password}`)
    .pipe(catchError(this.handleError<NeedCheckout>('removeNeedToFundingBasket')));
  }

  checkout(username: string, password: string): Observable<NeedCheckout> {
    const url = `${this.needsCheckoutUrl}/checkout?username=${username}&password=${password}`;
    return this.http.put<NeedCheckout>(url,{username, password})
    .pipe(catchError(this.handleError<NeedCheckout>('checkout')));
  }

  emitAddToCartEvent(): void {
    this.addToCartEvent.emit();
  }


/** Log a HeroService message with the MessageService */
private log(message: string) {
    this.messageService.add(`NeedService: ${message}`);
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
      this.messageService.add(error)
        console.error(error);
        this.log(`${operation} failed: ${error.message}`);
        return of(result as T);
    };
}
}
