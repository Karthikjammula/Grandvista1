# GrandVista Hospitality Inventory Control & Dynamic Room Reservation Platform

Microservices:
- eureka-server: service discovery (8761)
- api-gateway: API gateway (8080)
- auth-service: JWT authentication (8081)
- room-service: room inventory/availability (8082)
- booking-service: reservations (8083)
- payment-service: payment transactions (8084)
- frontend: React/Vite UI

Local prerequisites:
- Java 17+
- Maven 3.9+
- Node.js 20+
- Postman
- Git

Architecture:
Browser -> Vercel/Frontend -> API Gateway -> services
                         Eureka <-> services
Booking -> Room reservation -> Payment

Demo users:
- admin@grandvista.com / Admin@123
- user@grandvista.com / User@123

Important: this is a college-project starter. For real production use, use separate databases, secrets, HTTPS, a real payment provider, and stronger authorization/auditing.
