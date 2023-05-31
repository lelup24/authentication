import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AppService } from './app.service';
import { take } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  appService = inject(AppService);

  loginForm!: FormGroup;
  registrationForm!: FormGroup;
  isLoggedIn: boolean = false;
  registrationSuccess: boolean = false;
  name = '';
  roles: string[] = [];
  unsecure!: string;
  secure!: string;
  secureAdmin!: string;
  showRegistration = false;

  ngOnInit(): void {
    if (localStorage.getItem('auth-token') !== '') {
      this.isLoggedIn = true;
      this.setName();
    }
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
    });

    this.registrationForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
    });
  }

  login() {
    this.appService
      .login(this.loginForm.value.username, this.loginForm.value.password)
      .pipe(take(1))
      .subscribe((response) => {
        localStorage.setItem(
          'auth-token',
          response.headers.get('auth-token') || ''
        );
        if (localStorage.getItem('auth-token') !== '') {
          this.isLoggedIn = true;
          this.setName();
        }
      });
  }

  register() {
    this.appService
      .register(
        this.registrationForm.value.username,
        this.registrationForm.value.password
      )
      .pipe(take(1))
      .subscribe((response) => {
        this.registrationSuccess = true;
      });
  }

  logout() {
    localStorage.setItem('auth-token', '');
    this.isLoggedIn = false;
  }

  fetchUnsecure() {
    this.appService
      .fetchUnsecureEndpoint()
      .pipe(take(1))
      .subscribe((response) => {
        this.unsecure = response.msg;
      });
  }

  fetchSecure() {
    this.appService
      .fetchSecureEndpoint()
      .pipe(take(1))
      .subscribe({
        next: (response) => (this.secure = response.msg),
        error: (err) => {
          this.secure = err.statusText;
        },
      });
  }

  fetchSecureAdmin() {
    this.appService
      .fetchSecureAdminEndpoint()
      .pipe(take(1))
      .subscribe({
        next: (response) => (this.secureAdmin = response.msg),
        error: (err) => {
          this.secureAdmin = err.statusText;
        },
      });
  }

  private setName() {
    const helper = new JwtHelperService();
    const decodedToken = helper.decodeToken(
      localStorage.getItem('auth-token') || ''
    );
    this.name = decodedToken?.sub;
    this.roles = decodedToken['roles'];
  }
}
