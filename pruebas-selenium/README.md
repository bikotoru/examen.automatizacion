# Pruebas Selenium para Sitio Login

Este proyecto contiene pruebas automatizadas con Selenium para probar el login de cualquier sitio web desplegado.

## Características

- **4 pruebas automatizadas:**
  - Login exitoso con credenciales correctas
  - Login fallido con usuario incorrecto
  - Login fallido con contraseña incorrecta
  - Validación de campos vacíos

- **Capturas de pantalla automáticas** en cada paso de las pruebas
- **Volumen Docker** para acceder a las screenshots desde el host
- **URL configurable** mediante variable de entorno (OBLIGATORIA)
- **Independiente** - No requiere el código fuente del sitio a probar

## Ejecución Local

```bash
cd pruebas-selenium
# Configurar la URL del sitio a probar
export SITIO_URL=http://localhost:8080
mvn test
```

## Ejecución con Docker

**IMPORTANTE:** Siempre debe proporcionar la variable `SITIO_URL` al ejecutar las pruebas.

### Construcción de la imagen:
```bash
cd pruebas-selenium
docker build -t pruebas-selenium .
```

### Ejecución contra un sitio desplegado:
```bash
# Ejemplo contra localhost
docker run -e SITIO_URL=http://host.docker.internal:8080 -v $(pwd)/screenshots:/app/screenshots pruebas-selenium

# Ejemplo contra un servidor remoto
docker run -e SITIO_URL=http://mi-servidor.com:8080 -v $(pwd)/screenshots:/app/screenshots pruebas-selenium

# Ejemplo contra un contenedor Docker (usando nombre de red)
docker run --network mi-red -e SITIO_URL=http://sitio-container:8080 -v $(pwd)/screenshots:/app/screenshots pruebas-selenium
```

## Validación de URL

El Dockerfile incluye validación automática:
- Si no se proporciona `SITIO_URL`, las pruebas fallarán con un mensaje de error
- Muestra ejemplos de uso correcto

## Screenshots

Las capturas de pantalla se guardan automáticamente en el directorio `screenshots/` y incluyen:
- `01_pagina_inicial.png` - Estado inicial de la página
- `02_formulario_completado.png` - Formulario con datos válidos
- `03_login_exitoso.png` - Página de éxito
- `04-11_*.png` - Capturas de pruebas de error y validación

## Casos de Uso

Este contenedor es ideal para:
- **CI/CD** - Pruebas automáticas contra ambientes de staging/producción
- **Monitoreo** - Verificación periódica de funcionalidad
- **Testing independiente** - Sin necesidad del código fuente del sitio