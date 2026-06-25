docker build -t smart-clinic-backend .

docker run -d -p 8080:8080 --name smart-clinic smart-clinic-backend
