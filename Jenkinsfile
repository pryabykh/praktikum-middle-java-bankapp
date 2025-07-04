pipeline {
    agent any

    stages {
        stage('Package accounts') {
            steps {
                sh "chmod +x -R ${env.WORKSPACE}"
                sh '''
                    cd accounts
                    ./mvnw clean package
                '''
            }
        }

        stage('Dockerize accounts') {
            steps {
                sh '''
                    docker build -t accounts-service:0.0.1-SNAPSHOT ./accounts
                '''
            }
        }

        stage('Push accounts image to minikube') {
            steps {
                sh '''
                    minikube image load accounts-service:0.0.1-SNAPSHOT
                '''
            }
        }

        stage('Package blocker') {
            steps {
                sh "chmod +x -R ${env.WORKSPACE}"
                sh '''
                    cd blocker
                    ./mvnw clean package
                '''
            }
        }

        stage('Dockerize blocker') {
            steps {
                sh '''
                    docker build -t blocker-service:0.0.1-SNAPSHOT ./blocker
                '''
            }
        }

        stage('Push blocker image to minikube') {
            steps {
                sh '''
                    minikube image load blocker-service:0.0.1-SNAPSHOT
                '''
            }
        }

        stage('Package cash') {
            steps {
                sh "chmod +x -R ${env.WORKSPACE}"
                sh '''
                    cd cash
                    ./mvnw clean package
                '''
            }
        }

        stage('Dockerize cash') {
            steps {
                sh '''
                    docker build -t cash-service:0.0.1-SNAPSHOT ./cash
                '''
            }
        }

        stage('Push cash image to minikube') {
            steps {
                sh '''
                    minikube image load cash-service:0.0.1-SNAPSHOT
                '''
            }
        }

        stage('Package exchange') {
            steps {
                sh "chmod +x -R ${env.WORKSPACE}"
                sh '''
                    cd exchange
                    ./mvnw clean package
                '''
            }
        }

        stage('Dockerize exchange') {
            steps {
                sh '''
                    docker build -t exchange-service:0.0.1-SNAPSHOT ./exchange
                '''
            }
        }

        stage('Push exchange image to minikube') {
            steps {
                sh '''
                    minikube image load exchange-service:0.0.1-SNAPSHOT
                '''
            }
        }

        stage('Package exchange-generator') {
            steps {
                sh "chmod +x -R ${env.WORKSPACE}"
                sh '''
                    cd exchange-generator
                    ./mvnw clean package
                '''
            }
        }

        stage('Dockerize exchange-generator') {
            steps {
                sh '''
                    docker build -t exchange-generator-service:0.0.1-SNAPSHOT ./exchange-generator
                '''
            }
        }

        stage('Push exchange-generator image to minikube') {
            steps {
                sh '''
                    minikube image load exchange-generator-service:0.0.1-SNAPSHOT
                '''
            }
        }

        stage('Package front') {
            steps {
                sh "chmod +x -R ${env.WORKSPACE}"
                sh '''
                    cd front
                    ./mvnw clean package
                '''
            }
        }

        stage('Dockerize front') {
            steps {
                sh '''
                    docker build -t front-service:0.0.1-SNAPSHOT ./front
                '''
            }
        }

        stage('Push front image to minikube') {
            steps {
                sh '''
                    minikube image load front-service:0.0.1-SNAPSHOT
                '''
            }
        }

        stage('Package notifications') {
            steps {
                sh "chmod +x -R ${env.WORKSPACE}"
                sh '''
                    cd notifications
                    ./mvnw clean package
                '''
            }
        }

        stage('Dockerize notifications') {
            steps {
                sh '''
                    docker build -t notifications-service:0.0.1-SNAPSHOT ./notifications
                '''
            }
        }

        stage('Push notifications image to minikube') {
            steps {
                sh '''
                    minikube image load notifications-service:0.0.1-SNAPSHOT
                '''
            }
        }

        stage('Package transfer') {
            steps {
                sh "chmod +x -R ${env.WORKSPACE}"
                sh '''
                    cd transfer
                    ./mvnw clean package
                '''
            }
        }

        stage('Dockerize transfer') {
            steps {
                sh '''
                    docker build -t transfer-service:0.0.1-SNAPSHOT ./transfer
                '''
            }
        }

        stage('Push transfer image to minikube') {
            steps {
                sh '''
                    minikube image load transfer-service:0.0.1-SNAPSHOT
                '''
            }
        }

        stage('Dockerize keycloak') {
            steps {
                sh '''
                    docker build -t keycloak:pr ./keycloak
                '''
            }
        }

        stage('Push keycloak image to minikube') {
            steps {
                sh '''
                    minikube image load keycloak:pr
                '''
            }
        }

        stage('Установка Helm') {
            steps {
                sh '''
                    helm dependency update ./k8s
                    helm install myapp ./k8s
                '''
            }
        }
    }
}