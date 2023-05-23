import {inject, Injectable} from "@angular/core";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AppService {

  http = inject(HttpClient);

  login(username: string, password: string): Observable<void> {
    return this.http.post<void>('/api/login', {username, password});
  }

  register(username: string, password: string): Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>('/api/register', {username, password}, {
      observe: 'response'
    });
  }

}
