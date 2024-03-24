import { EventEmitter, Injectable } from '@angular/core';
import { Need } from './need';
import { NEEDS } from './mock-needs';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';


@Injectable({
    providedIn: 'root'
})
export class NeedService {
    private needsUrl = 'http://localhost:8080/needs/cupboard'// URL to web API

    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    constructor(
        private http: HttpClient,
        private messageService: MessageService) { }

    // Function to get a single need from the cupboard
    getNeedFromCupboard(id: number): Observable<Need> {
        const url = `${this.needsUrl}/${id}`;
        return this.http.get<Need>(url).pipe(
            catchError(this.handleError<Need>('getNeedFromCupboard'))
    );
    }

    // Function to get all needs from the cupboard
    getNeedsFromCupboard(): Observable<Need[]> {
        const url = `${this.needsUrl}`;
        return this.http.get<Need[]>(url).pipe(
            catchError(this.handleError<Need[]>('getNeedsFromCupboard'))
        );
    }

    // Function to find matching needs from the cupboard based on name
    findMatchingNeedsFromCupboard(name: string): Observable<Need[]> {
        const url = `${this.needsUrl}/?name=${name}`;
        return this.http.get<Need[]>(url).pipe(
            catchError(this.handleError<Need[]>('findMatchingNeedsFromCupboard'))
        );
    }

    // Function to create a new need in the cupboard
    createNeedInCupboard(need: Need, username: string, password: string): Observable<Need> {
        const url = `${this.needsUrl}?username=${username}&password=${password}`
        return this.http.post<Need>(url, need).pipe(
            catchError(this.handleError<Need>('createNeedInCupboard'))
        );
    }

    // Function to update a need in the cupboard
    updateNeedInCupboard(need: Need, username: string, password: string): Observable<Need> {
        const url = `${this.needsUrl}?username=${username}&password=${password}`
        return this.http.put<Need>(url, need).pipe(
            catchError(this.handleError<Need>('updateNeedInCupboard'))
        );
    }

    // Function to delete a need from the cupboard
    deleteNeedFromCupboard(id: number, username: string, password: string): Observable<boolean> {
        const url = `${this.needsUrl}/${id}?username=${username}&password=${password}`;
        return this.http.delete<boolean>(url).pipe(
            catchError(this.handleError<boolean>('deleteNeedFromCupboard'))
        );
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
            console.error(error);
            this.log(`${operation} failed: ${error.message}`);
            return of(result as T);
        };
    }
}
