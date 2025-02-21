# 🚀 Microservices Project with Kubernetes & Minikube

## 📌 **Project Overview**

This project is a **microservices-based system** deployed using **Spring Boot, Spring Cloud, Eureka, API Gateway, and Kubernetes**. It consists of the following services:

- **Eureka Server**: Service discovery and registry.
- **API Gateway**: Centralized entry point for all microservices, handles routing and fallback mechanisms.
- **Patient Service**: Manages patient records (CRUD operations).
- **Practitioner Service**: Manages practitioner records (CRUD operations).
- **Kubernetes Deployment**: Each service is containerized and deployed inside a **Minikube Kubernetes cluster**.
- **Swagger API Documentation**: Each microservice is documented with Swagger UI for easy API testing.

---

## 📜 **Features**

- ✅ **Swagger API Documentation**: Interactive API testing using Swagger UI.
- ✅ **Service Discovery**: Eureka Server allows microservices to discover each other dynamically.
- ✅ **API Gateway**: Routes all requests and handles fallbacks using Resilience4J. 
- ✅ **Microservices Architecture**: Independent, modular services with dedicated responsibilities.
- ✅ **Resilience & Fault Tolerance**: Circuit breaker and rate limiter using Resilience4J.
- ✅ **Load Balancing**: API Gateway routes traffic to multiple instances of services. 
- ✅ **Containerized Deployment**: Docker & Kubernetes manage and orchestrate services. 
- ✅ **Scalability**: Multiple replicas of microservices can be deployed.

---

## 🛠 **Project Structure**

```
├── eureka-server/           # Eureka Discovery Server
├── api-gateway/             # API Gateway (Spring Cloud Gateway)
├── patient-service/         # Patient Management Microservice
├── practitioner-service/    # Practitioner Management Microservice
├── k8s/                     # Kubernetes Deployment YAMLs
│   ├── eureka-deployment.yml
│   ├── eureka-service.yml
│   ├── patient-deployment.yml
│   ├── patient-service.yml
│   ├── practitioner-deployment.yml
│   ├── practitioner-service.yml
│   ├── api-gateway-deployment.yml
│   ├── api-gateway-service.yml
├── deploy.sh                # Deployment automation script
├── README.md                # Project documentation
```

---

## 🚀 **Deployment Instructions**

### **1️⃣ Prerequisites**

Ensure you have the following installed:

- [Docker](https://www.docker.com/get-started)
- [Minikube](https://minikube.sigs.k8s.io/docs/start/)
- [kubectl](https://kubernetes.io/docs/tasks/tools/)
- [Java 17+](https://adoptopenjdk.net/)
- [Maven](https://maven.apache.org/)

---

### **2️⃣ Start Minikube**

```sh
minikube start --driver=docker
```

If using Minikube for the first time, enable ingress (optional):

```sh
minikube addons enable ingress
```

---

### **3️⃣ Build & Deploy Microservices**

Run the `` script to **build images, load them into Minikube, and deploy all services**:

```sh
chmod +x deploy.sh  # Make the script executable
./deploy.sh         # Run the deployment script
```

---

### **4️⃣ Verify Deployment**

Check if all pods are running:

```sh
kubectl get pods
```

Expected Output:

```
NAME                                   READY   STATUS    RESTARTS   AGE
api-gateway-xxxxx                      1/1     Running   0          1m
eureka-server-xxxxx                    1/1     Running   0          1m
patient-service-xxxxx                  1/1     Running   0          1m
practitioner-service-xxxxx             1/1     Running   0          1m
```

Check services:

```sh
kubectl get services
```

Expected Output:

```
NAME                 TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
eureka-service       ClusterIP   10.96.230.42     <none>        8761/TCP         5m
patient-service      ClusterIP   10.96.230.43     <none>        8081/TCP         5m
practitioner-service ClusterIP   10.96.230.44     <none>        8082/TCP         5m
api-gateway         ClusterIP   10.96.230.45     <none>        8080/TCP         5m
```

---

### **5️⃣ Test the Microservices**

#### **API Gateway Routes**

Port forward the API Gateway:

```sh
kubectl port-forward svc/api-gateway 8080:8080
```

Now test the services:

```sh
curl http://localhost:8080/patients
curl http://localhost:8080/practitioner
```

Expected JSON response:

```json
[
  { "id": "1", "name": "Alice Dupont", "age": "30" },
  { "id": "2", "name": "Jean Martin", "age": "45" }
]
```

#### **Check Eureka Dashboard**

Port forward the Eureka Server:

```sh
kubectl port-forward svc/eureka-service 8761:8761
```

Now visit [**http://localhost:8761**](http://localhost:8761) and verify that **PRACTITIONER-SERVICE & PATIENT-SERVICE are registered**.

---

## 🛑 **Stopping & Cleaning Up**

### **Stop Minikube**

To stop all services and shut down Minikube:

```sh
minikube stop
```

### **Delete All Deployments**

To remove all microservices:

```sh
kubectl delete deployment --all
kubectl delete service --all
```

---

## 🎯 **Final Checklist**

✅ **Microservices Deployed Successfully in Kubernetes** 🎉\
✅ **Eureka Server Running & Microservices Registered** 🔗\
✅ **API Gateway Routes Functional** 🚀\
✅ **System is Fully Containerized & Scalable** 🏗️

Now, you have a fully functional **Spring Boot microservices project** running inside **Kubernetes with Minikube**! 🎉

🚀 **Feel free to contribute and improve this project!**

