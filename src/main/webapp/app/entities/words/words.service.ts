import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Words } from './words.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class WordsService {

    private resourceUrl = SERVER_API_URL + 'api/words';

    constructor(private http: Http) { }

    create(words: Words): Observable<Words> {
        const copy = this.convert(words);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(words: Words): Observable<Words> {
        const copy = this.convert(words);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Words> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Words.
     */
    private convertItemFromServer(json: any): Words {
        const entity: Words = Object.assign(new Words(), json);
        return entity;
    }

    /**
     * Convert a Words to a JSON which can be sent to the server.
     */
    private convert(words: Words): Words {
        const copy: Words = Object.assign({}, words);
        return copy;
    }
}
