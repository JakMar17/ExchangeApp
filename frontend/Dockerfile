# stage 1

FROM node:alpine AS my-app-build
WORKDIR /app
COPY . .
RUN npm install -g @angular/cli
RUN npm install
RUN ng build --prod

# stage 2

FROM nginx:alpine
RUN apk add bash
COPY --from=my-app-build /app/dist/exchange-app /usr/share/nginx/html
COPY --from=my-app-build /app/nginx.conf /etc/nginx/conf.d/
RUN rm /etc/nginx/conf.d/default.conf
EXPOSE 80