# Authentication Example using Spring Boot 3, Angular 16 and JWT

Authentication using Spring Boot 3 and Angular 16

* Clone

```bash
git clone https://github.com/lelup24/authentication.git
 ```

* run a postgresql database with docker or your own

```bash
docker compose up -d
```

* start backend or run in your IDE

```bash
./gradlew bootRun
```

* go to backend folder and run

```bash
npm i
```

* go to frontend folder and start frontend

```bash
ng serve
```

## RSA

```bash
openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:4096
```

```bash
openssl rsa -pubout -in private_key.pem -out public_key.pem
```

# Deployment

## Server Configuration

### Create new sudo user
```bash
adduser peter
usermod -aG sudo peter
```

### generate SSH-key
```bash
ssh-keygen -b 4096
```

Login to Server

```bash
mkdir -p ~/.ssh
sudo nano authorized_keys
chown -R peter ~/.ssh
chmod -R 700 ~/.ssh
```

### Create Config in .ssh

```bash
sudo nano ~/.ssh/config
```

Add this to the config-file

```
Host tutorial
HostName 85.215.114.254
IdentityFile ~/.ssh/tutorial
User peter
```

### SSH-Konfiguration

```bash
sudo nano /etc/ssh/sshd_config
```

Change this

```
PasswordAuthentication no
```

```bash
sudo systemctl restart ssh
```

### Installing Backend

```bash
sudo apt-get update && sudo apt-get upgrade
sudo apt install -y openjdk-17-jdk
```

### Postgres

```bash
sudo apt install postgresql postgresql-contrib
sudo -u postgres psql
\password postgres
```

```bash
ssh -L 5555:localhost:5432 tutorial
```

### Backend (Spring Boot)

```bash
mkdir /var/backend
sudo chown peter /var/backend
sudo chmod 700 /var/backend
scp backend-0.0.1-SNAPSHOT.jar tutorial:/var/backend
sudo nano /etc/systemd/system/tutorial.service
```

Füge den folgenden Inhalt hinzu:

```
[Unit]
Description=Tutorial
After=syslog.target

[Service]
User=peter
ExecStart=/usr/bin/java -Duser.timezone=CET -DDB_URL=jdbc:postgresql://localhost:5432/tutorial -Dspring.profiles.active=prod -jar /var/backend/backend-0.0.1-SNAPSHOT.jar SuccessExitStatus=143

Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

```bash
sudo systemctl start tutorial.service
curl http://localhost:8080/api/v1/unsecured
```

### Frontend (Angular)

```bash
sudo apt install nginx
sudo ufw allow 'Nginx Full'
sudo ufw allow 22
systemctl status nginx
```

```bash
cd /var/www/html/
sudo mkdir frontend
sudo chown -R peter frontend/
sudo chmod -R 755 frontend/
ng build
scp -r dist/frontend/* tutorial:/var/www/html/frontend
cd /etc/nginx/sites-available
sudo nano tutorial
```

Füge den folgenden Inhalt hinzu:

```nginx
server {
        listen 80;
        listen [::]:80;

        root /var/www/html/frontend;
        index index.html;

        server_name talentschmiede.org;

        location / {
                # First attempt to serve request as file, then
                # as directory, then fall back to displaying a 404.
                try_files $uri$args $uri$args/ /index.html;
        }

        location /api {
               proxy_pass http://localhost:8080;
               proxy_set_header Host $host;
               proxy_set_header X-Real-IP $remote_addr;
               proxy_set_header X-Forwarded-Proto $scheme;
               proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
       }


       location ~* \.(ico|pdf|flv|jpg|jpeg|png|gif|js|css|swf)$ {
        expires 1h;
        add_header Cache-Control "public, no-transform";
      }
}
```

```bash
sudo ln -s /etc/nginx/sites-available/tutorial /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

```bash
sudo snap install core
sudo snap refresh core
sudo snap install --classic certbot
sudo ln -s /snap/bin/certbot /usr/bin/certbot
sudo certbot --nginx
```

## Deploy with Docker

### install docker

```bash
sudo apt-get update
sudo apt-get install ca-certificates curl gnupg
```

```bash
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg
```

```bash
echo \
"deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
"$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | \
sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

```bash
sudo apt-get update
```

```bash
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

### build

```bash
cd backend
docker build -t tutorial-backend:latest .
```

```bash
cd frontend
docker build -t tutorial-frontend:latest .
```

### publish

````bash
docker login
````

```bash
docker tag tutorial-backend:latest lelup24/tutorial-backend:latest
```

````bash
docker push lelup24/tutorial-backend:latest
````

```bash
docker tag tutorial-frontend:latest lelup24/tutorial-frontend:latest
```

```bash
docker push lelup24/tutorial-frontend:latest
```

```bash
docker compose -f docker-compose.prod.yml up
```
