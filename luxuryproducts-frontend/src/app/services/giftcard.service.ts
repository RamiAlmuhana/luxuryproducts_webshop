import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class GiftcardService {
    private baseUrl: string = environment.base_url + "/giftcards";

    constructor(private http: HttpClient) { }

    public buyGiftCard(giftCardCode: string, discountAmount: number): Observable<any> {
        return this.http.post<any>(this.baseUrl, { code: giftCardCode, discountAmount });
    }
}