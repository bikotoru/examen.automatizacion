FROM openjdk:17-jdk-slim

# Instalar dependencias necesarias
RUN apt-get update && \
    apt-get install -y wget gnupg unzip curl maven && \
    wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copiar archivos del proyecto
COPY pom.xml .
COPY src ./src

# Variable de entorno para la URL del sitio a probar (debe ser proporcionada al ejecutar)
ENV SITIO_URL=""

# Crear directorio para screenshots
RUN mkdir -p /app/screenshots

# Compilar las pruebas
RUN mvn clean test-compile

# Script para validar que se proporcione la URL
RUN echo '#!/bin/bash\nif [ -z "$SITIO_URL" ]; then\n  echo "ERROR: Debe proporcionar la variable SITIO_URL"\n  echo "Ejemplo: docker run -e SITIO_URL=http://mi-sitio:8080 -v \$(pwd)/screenshots:/app/screenshots pruebas-selenium"\n  exit 1\nfi\necho "Ejecutando pruebas contra: $SITIO_URL"\nmvn test' > /app/run-tests.sh && \
    chmod +x /app/run-tests.sh

# Comando por defecto para ejecutar las pruebas
CMD ["/app/run-tests.sh"]

# Volumen para screenshots
VOLUME ["/app/screenshots"]