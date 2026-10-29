# Deployment

## Local
mvn clean package -DskipTests
Run Eureka first, then auth/room/booking/payment, then gateway.
Start frontend with npm install && npm run dev.

## Vercel
Host only the React frontend on Vercel.
Set VITE_API_URL to the public API Gateway URL.

## Backend
Deploy Eureka, Auth, Room, Booking, Payment and Gateway as separate Java services on a Java-capable cloud platform.
Set EUREKA_URL on every client, the same JWT_SECRET on all JWT-validating services, and FRONTEND_URL on the Gateway.
Use HTTPS URLs in production.
