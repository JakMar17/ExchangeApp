import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Notification } from 'src/app/components/notifications/models/notification-interface';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class NotificationsApiService {
  private baseUrl = environment.BASE_API_URL + 'notification';
  constructor(private http: HttpClient) {}

  public saveNotification(
    notification: Notification,
    personalNumber: string,
    courseId: number | null
  ): Observable<Notification> {
    let headers = new HttpHeaders({
      'Personal-Number': personalNumber,
    });

    if (courseId != null) headers.append('Course-Id', courseId.toString());
    return this.http.post<Notification>(this.baseUrl, notification, {
      headers,
    });
  }

  public deleteNotification(
    notificationId: number,
    personalNumber: string,
    courseId: number | null
  ): Observable<any> {
    let headers = new HttpHeaders({
      'Personal-Number': personalNumber,
    });

    if (courseId != null) headers.append('Course-Id', courseId.toString());

    return this.http.delete<any>(
      this.baseUrl + '/delete?notificationId=' + notificationId,
      { headers }
    );
  }

  public getDashoboardNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(this.baseUrl + '/dashboard');
  }
}
