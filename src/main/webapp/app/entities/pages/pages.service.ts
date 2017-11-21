import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Pages } from './pages.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PagesService {

    private resourceUrl = SERVER_API_URL + 'api/pages';

    constructor(private http: Http) { }

    create(pages: Pages): Observable<Pages> {
        const copy = this.convert(pages);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(pages: Pages): Observable<Pages> {
        const copy = this.convert(pages);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Pages> {
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
     * Convert a returned JSON object to Pages.
     */
    private convertItemFromServer(json: any): Pages {
        const entity: Pages = Object.assign(new Pages(), json);
        return entity;
    }

    /**
     * Convert a Pages to a JSON which can be sent to the server.
     */
    private convert(pages: Pages): Pages {
        const copy: Pages = Object.assign({}, pages);
        return copy;
    }
}
