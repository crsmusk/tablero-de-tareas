# Changelog

## [1.2.0](https://github.com/crsmusk/tablero-de-tareas/compare/v1.1.0...v1.2.0) (2026-04-24)


### Features

* implement granular authorization, stakeholder behavioral modeli… ([80a2eff](https://github.com/crsmusk/tablero-de-tareas/commit/80a2eff1ebf4b2de7dcad403569111cd7cc69a58))
* implement granular authorization, stakeholder behavioral modeling, and technical auditing. Updated documentation in /docs and enabled public profile registration. ([fb9df31](https://github.com/crsmusk/tablero-de-tareas/commit/fb9df31c9a9d053c6c43367b45f074e1c526a373))
* implement JWT-based authentication and role management system ([0b3e833](https://github.com/crsmusk/tablero-de-tareas/commit/0b3e833503d42d49a5f116c7c448a914e989a0b5))
* Implementar el flujo de autenticación JWT, los manejadores de seguridad personalizados y el DTO de aprobación. ([ff73541](https://github.com/crsmusk/tablero-de-tareas/commit/ff73541b0191ae74df4ca6b5f09b806306a52dca))
* **security:** embed userId in JWT claims and extract it into SecurityContext ([ad14f06](https://github.com/crsmusk/tablero-de-tareas/commit/ad14f06af2ac501b9801b5a5a7d3cb85eddeff9f))
* **tasks:** filter tasks by project and deliverables by task, and add task summary endpoint ([e3699fc](https://github.com/crsmusk/tablero-de-tareas/commit/e3699fcc012ab56f2df9a020e0f086ba380f729a))


### Bug Fixes

* **role:** handle missing roles by returning a new entity instead of throwing exception ([ff09050](https://github.com/crsmusk/tablero-de-tareas/commit/ff090501410bf4711521d52134bf5cec062b194b))


### Documentation

* actualizar documentación y estándares de seguridad JWT ([7cc3b63](https://github.com/crsmusk/tablero-de-tareas/commit/7cc3b635c917c24b3079e69d90daa08ef2df81a0))
* actualizar documentación y estándares de seguridad JWT ([a066d9f](https://github.com/crsmusk/tablero-de-tareas/commit/a066d9f61bb5d4647ae5a5ebd50f0913e2a7687c))

## [1.1.0](https://github.com/crsmusk/tablero-de-tareas/compare/v1.0.0...v1.1.0) (2026-04-04)


### Features

* agregar dependencias y configuracion base de swagger openapi ([de6cf0a](https://github.com/crsmusk/tablero-de-tareas/commit/de6cf0af3ed86436ca8a9925de63d96bc7e1256d))
* implementar anotaciones swagger openapi en los controladores ([ffa6978](https://github.com/crsmusk/tablero-de-tareas/commit/ffa69784314c0220115dddafaa4a91a98587f324))
* Implementar un sistema de aprobación con servicio, controlador, DTOs, pruebas, configuración de Swagger y documentación de la API. ([8f844ce](https://github.com/crsmusk/tablero-de-tareas/commit/8f844ce531f676b760d5ca8b39f8defc39eb4b01))


### Documentation

* actualizar manuales de api, contributing y agent para requerir OpenAPI ([a8d2099](https://github.com/crsmusk/tablero-de-tareas/commit/a8d20998abbc5f1e1e713f838af3e7f18e453f47))

## 1.0.0 (2026-04-01)


### Features

* add AprovacionDtoEntrada field and AprovacionMapper ([28233e0](https://github.com/crsmusk/tablero-de-tareas/commit/28233e0a911722f29216395562d79a0afd24964d))
* crear AlmacenamientoService para centralizar operaciones de archivos ([6052a75](https://github.com/crsmusk/tablero-de-tareas/commit/6052a756585d5321645c3ff3323079a5f03db995))
* implement Aprovacion service and interface ([afe2071](https://github.com/crsmusk/tablero-de-tareas/commit/afe20718ed4b486d392343ab5eac840dc24db79b))
* implement AprovacionController ([6d79fb4](https://github.com/crsmusk/tablero-de-tareas/commit/6d79fb47ee9ec8a663aa6e01415fb8b7109f4e61))
* implement service layer for file storage, task approvals, and deliverable management ([20bf9f6](https://github.com/crsmusk/tablero-de-tareas/commit/20bf9f68a57714d19a03d0ac447065afa2211873))
* implementar validaciones Jakarta y anotación @Valid en DTOs de … ([2c9d58f](https://github.com/crsmusk/tablero-de-tareas/commit/2c9d58f7e629432627a669b67a6f77d25e6fa954))
* implementar validaciones Jakarta y anotación @Valid en DTOs de entrada y controladores ([45e80b4](https://github.com/crsmusk/tablero-de-tareas/commit/45e80b4a742dd6187127dbf27f332f6cad2b10dc))
* standardise ProyectoControlador responses to ResponseEntity ([a4bcc3a](https://github.com/crsmusk/tablero-de-tareas/commit/a4bcc3a0d3a131c284e7f35d75b40fea614f5c71))


### Bug Fixes

* corregir imports accidentales y limpiar lints en servicio y tests ([c0c6aa3](https://github.com/crsmusk/tablero-de-tareas/commit/c0c6aa3cf44c31d8777202dee378f9f6f70dddc2))
* **excepciones:** ocultar datos en errores 500, agregar log Slf4j y manejar TypeMismatch ([3c68114](https://github.com/crsmusk/tablero-de-tareas/commit/3c681144b8791ab1f38844f4f040f60c14d1807e))
* rename PerfiServiceImpl to PerfilServiceImpl ([be96082](https://github.com/crsmusk/tablero-de-tareas/commit/be9608292c4e30475ea0f4faf0a11dac9f6b3252))
* **servicio:** estandarizar el manejo de excepciones y agregar transaccionalidad ([1c79f7c](https://github.com/crsmusk/tablero-de-tareas/commit/1c79f7c08ca27ad76dedcdc8384a8d2add99d643))
* **servicio:** estandarizar el manejo de excepciones y agregar transaccionalidad ([ea279d0](https://github.com/crsmusk/tablero-de-tareas/commit/ea279d09d8a605e86bc1375242f63b4a92b4f29d))
* update title search query in TareaRepositorio ([a902753](https://github.com/crsmusk/tablero-de-tareas/commit/a902753b9ee1574f30950822a2ef5941abbd0f7e))


### Documentation

* actualizar agent.md con estrategia de testing y flujo de git ([f3f66c0](https://github.com/crsmusk/tablero-de-tareas/commit/f3f66c0d906051a7b3b22ac41c33be97202ad41d))
* actualizar agent.md con estrategia de testing y flujo de git ([6143fec](https://github.com/crsmusk/tablero-de-tareas/commit/6143fec688989297a3779f0008fdb4972d7c4480))
* actualizar agent.md con nueva regla de idioma y limpieza final de código ([4aa6e29](https://github.com/crsmusk/tablero-de-tareas/commit/4aa6e2988cd918c9499c2bf746f3671864d012d9))
* actualizar agent.md para requerir mensajes en español y limpieza de lints ([a8c2c1a](https://github.com/crsmusk/tablero-de-tareas/commit/a8c2c1a82b97f57ac5dfc117c4a7b9e8a989ce52))
* agregar documentacion del proyecto y actualizar README ([a47ec5c](https://github.com/crsmusk/tablero-de-tareas/commit/a47ec5c9f2264c65e954fefc70076a4a0f9e651b))
* agregar documentacion del proyecto y actualizar README ([a1cace3](https://github.com/crsmusk/tablero-de-tareas/commit/a1cace393796a520ed365f6cb280a60133c676d5))
* agregar product-spec.md ([65d6c2d](https://github.com/crsmusk/tablero-de-tareas/commit/65d6c2d9bd5132e4917e55356960a1727cc40114))
* agregar product-spec.md ([72a9f08](https://github.com/crsmusk/tablero-de-tareas/commit/72a9f08c7e5c0613b40861f1a6896e707bff9a4a))
