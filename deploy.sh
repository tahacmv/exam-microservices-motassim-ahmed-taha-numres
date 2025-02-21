#!/bin/bash
minikube delete
# 🚀 Start Minikube
echo "🔥 Starting Minikube..."
minikube start --driver=docker --extra-config=apiserver.authorization-mode=Node,RBAC

kubectl create clusterrolebinding minikube-admin \
  --clusterrole=cluster-admin \
  --serviceaccount=kube-system:default

kubectl delete pod --all -n kube-system

# 🏗️ Build images inside  minikube's docker env
echo "📦 Building Images inside  minikube's docker environment..."
eval $(minikube docker-env)

# 💡 Enable Ingress (if needed)
# minikube addons enable ingress

# 🏗️ Build Docker Images
echo "📦 Building Docker images..."
docker build -t eureka-server ./eureka-server
docker build -t patient-service ./patient-service
docker build -t practitioner-service ./practitioner-service
docker build -t api-gateway ./api-gateway

# 🚢 Load Images into Minikube
echo "⬆️ Loading images into Minikube..."
minikube image load eureka-server
minikube image load patient-service
minikube image load practitioner-service
minikube image load api-gateway

# 📌 Apply Kubernetes Deployments & Services
echo "🚀 Deploying microservices to Minikube..."
kubectl apply -f eureka-server/eureka-deployment.yml
kubectl apply -f eureka-server/eureka-service.yml
kubectl apply -f patient-service/patient-deployment.yml
kubectl apply -f patient-service/patient-service.yml
kubectl apply -f practitioner-service/practitioner-deployment.yml
kubectl apply -f practitioner-service/practitioner-service.yml
kubectl apply -f api-gateway/gateway-deployment.yml
kubectl apply -f api-gateway/gateway-service.yml

# 🕒 Wait for Pods to Start
echo "⏳ Waiting for all pods to be ready..."
kubectl wait --for=condition=ready pod --all --timeout=300s

# 🌐 Get API Gateway URL
echo "✅ Deployment complete!"
echo "📡 API Gateway URL:"
minikube service api-gateway --url

echo "📡 Eureka Dashboard:"
minikube service eureka-service --url
