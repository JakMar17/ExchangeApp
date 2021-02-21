# stage 1

FROM node:alpine AS my-app-build
WORKDIR /app
COPY . .
RUN npm install -g @angular/cli
RUN npm install
RUN ng build --prod

# stage 2

FROM nginx:alpine
COPY --from=my-app-build /app/dist/exchange-app /usr/share/nginx/html
EXPOSE 80