Para rodar o projeto:

Inicie o minikube: minikube start

Configure o docker para usar o Minikube: eval $(minikube docker-env)

De build das imagens navegando até a pasta raiz do projeto: 
  docker build -t atproduct:1.0 ./product-service
  docker build -t atorder:1.0 ./order-service
  docker build -t atpurchase:1.0 ./purchase-service

Aplicar o service e o deploy de cada aplicação:

  kubectl apply -f k8/product-deployment.yaml
  kubectl apply -f k8/product-service.yaml

  kubectl apply -f k8/purchase-deployment.yaml
  kubectl apply -f k8/purchase-service.yaml

  kubectl apply -f k8/order-deployment.yaml
  kubectl apply -f k8/order-service.yaml

  Pegar o Ip do Minikube: minikube ip

  Fazer a chamada no postman ou Bruno: http://192.168.49.2:30081/order/1?quantity=2 (usei o IP default do minikube, ele pode ser diferente por isso botei o passo anterior de pegar o IP, modificar o IP com o IP que saiu ali)
