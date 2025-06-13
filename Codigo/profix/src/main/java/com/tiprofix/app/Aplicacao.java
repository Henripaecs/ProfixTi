package com.tiprofix.app;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.tiprofix.models.Usuario;
import com.tiprofix.models.Prestador;
import com.tiprofix.models.Pedido;
import com.tiprofix.services.UsuarioService;
import com.tiprofix.services.PrestadorService;
import com.tiprofix.services.PedidoService;
import com.tiprofix.services.HashUtil; 

public class Aplicacao {

    public static void main(String[] args) {
        port(8081);
        staticFiles.location("/front-end");

        // === INÍCIO: Configuração CORS ===
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "http://localhost:5500"); // ou onde seu front roda
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type");
            response.header("Access-Control-Allow-Credentials", "true");
        });

        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });
        // === FIM: Configuração CORS ===

        Gson gson = new Gson();

        // Health check
        get("/health", (req, res) -> {
            res.type("application/json");
            Map<String, String> message = new HashMap<>();
            message.put("message", "rodando");
            return gson.toJson(message);
        });

        // Rotas Usuário (/usuarios)
        path("/usuarios", () -> {
            UsuarioService usuarioService = new UsuarioService();

            get("", (req, res) -> {
                res.type("application/json");
                var usuarios = usuarioService.pegarTodosOsUsuarios();
                res.status(200);
                return gson.toJson(usuarios);
            });

            get("/:id", (req, res) -> {
                res.type("application/json");
                int id = Integer.parseInt(req.params(":id"));
                Usuario usuario = usuarioService.pegarUsuarioId(id);
                if (usuario == null) {
                    res.status(404);
                    return gson.toJson("Usuário não encontrado");
                }
                res.status(200);
                return gson.toJson(usuario);
            });

            post("", (req, res) -> {
                res.type("application/json");
                Usuario novoUsuario = gson.fromJson(req.body(), Usuario.class);

                // Gerar hash MD5 da senha
                String senhaHash = HashUtil.hashMD5(novoUsuario.getSenha());
                novoUsuario.setSenha(senhaHash);
                Usuario criado = usuarioService.criaUsuario(novoUsuario);
                if (criado == null) {
                    res.status(400);
                    return gson.toJson("Usuário inválido");
                }
                res.status(201);
                return gson.toJson(criado);
            });

            put("/:id", (req, res) -> {
                res.type("application/json");
                int id = Integer.parseInt(req.params(":id"));
                Usuario usuarioAtualizado = gson.fromJson(req.body(), Usuario.class);

                // Gerar hash MD5 da senha
                String senhaHash = HashUtil.hashMD5(usuarioAtualizado.getSenha());
                usuarioAtualizado.setSenha(senhaHash);
                boolean sucesso = usuarioService.atualizarUsuario(usuarioAtualizado, id);
                if (!sucesso) {
                    res.status(400);
                    return gson.toJson("Falha ao atualizar usuário");
                }
                res.status(200);
                return gson.toJson(usuarioAtualizado);
            });

            delete("/:id", (req, res) -> {
                res.type("application/json");
                int id = Integer.parseInt(req.params(":id"));
                boolean sucesso = usuarioService.deletarUsuario(id);
                if (!sucesso) {
                    res.status(404);
                    return gson.toJson("Usuário não encontrado");
                }
                res.status(204);
                return "";
            });
        });

        // Rota de login (/login)
        post("/login", (req, res) -> {
            res.type("application/json");

            try {
                Map<String, String> dadosLogin = gson.fromJson(req.body(), Map.class);
                String email = dadosLogin.get("email");
                String senha = dadosLogin.get("senha");

                // Gerar hash MD5 da senha
                String senhaHash = HashUtil.hashMD5(senha);
                senha = senhaHash;

                UsuarioService usuarioService = new UsuarioService();
                Usuario usuario = usuarioService.autenticar(email, senha);

                if (usuario != null) {
                    req.session(true).attribute("usuarioLogado", usuario); 
                    res.status(200);
                    return gson.toJson(usuario);
                } else {
                    res.status(401);
                    return gson.toJson(Map.of("erro", "Credenciais inválidas"));
                }

            } catch (Exception e) {
                res.status(400);
                return gson.toJson(Map.of("erro", "Requisição inválida"));
            }
        });

        get("/logout", (req, res) -> {
            req.session().invalidate();
            res.status(200);
            res.type("application/json");
            return "{\"message\": \"Sessão encerrada com sucesso\"}";
        });

        get("/usuario-logado", (req, res) -> {
            res.type("application/json");
            Usuario usuario = req.session().attribute("usuarioLogado");

            if (usuario != null) {
                return gson.toJson(usuario);
            } else {
                res.status(401);
                return gson.toJson(Map.of("erro", "Usuário não logado"));
            }
        });

        get("/prestador-logado", (req, res) -> {
            res.type("application/json");
            Prestador prestador = req.session().attribute("prestadorLogado");
            if (prestador != null) {
                return gson.toJson(prestador);
            } else {
                res.status(401);
                return gson.toJson(Map.of("erro", "Prestador não logado"));
            }
        });

        // Rotas Prestador (/prestador)
        path("/prestador", () -> {
            PrestadorService prestadorService = new PrestadorService();

            get("", (req, res) -> {
                res.type("application/json");
                var prestadores = prestadorService.pegarTodosOsPrestadores();
                res.status(200);
                return gson.toJson(prestadores);
            });

            get("/:id", (req, res) -> {
                try {
                    int id = Integer.parseInt(req.params(":id"));
                    Prestador prestador = prestadorService.pegarPrestadorId(id);
                    if (prestador == null) {
                        res.status(404);
                        return gson.toJson("Prestador não encontrado");
                    }
                    res.status(200);
                    return gson.toJson(prestador);
                } catch (NumberFormatException e) {
                    res.status(400);
                    return gson.toJson("ID inválido, deve ser número");
                } catch (IllegalArgumentException e) {
                    res.status(400);
                    return gson.toJson(e.getMessage());
                } catch (Exception e) {
                    res.status(500);
                    return gson.toJson("Erro interno: " + e.getMessage());
                }
            });

            post("/login/prestador", (req, res) -> {
                res.type("application/json");

                try {
                    Map<String, String> dadosLogin = gson.fromJson(req.body(), Map.class);
                    String email = dadosLogin.get("email");
                    String senha = dadosLogin.get("senha");

                    // Gerar hash MD5 da senha
                    String senhaHash = HashUtil.hashMD5(senha);
                    senha = senhaHash;

                    Prestador prestador = prestadorService.autenticar(email, senha);

                    if (prestador != null) {
                        req.session(true).attribute("prestadorLogado", prestador);
                        res.status(200);
                        return gson.toJson(prestador);
                    } else {
                        res.status(401);
                        return gson.toJson(Map.of("erro", "Credenciais inválidas"));
                    }

                } catch (Exception e) {
                    res.status(400);
                    return gson.toJson(Map.of("erro", "Requisição inválida"));
                }
            });

            post("", (req, res) -> {
                res.type("application/json");
                Prestador novoPrestador = gson.fromJson(req.body(), Prestador.class);

                // Gerar hash MD5 da senha
                String senhaHash = HashUtil.hashMD5(novoPrestador.getSenha());
                novoPrestador.setSenha(senhaHash);

                Prestador criado = prestadorService.criaPrestador(novoPrestador);
                if (criado == null) {
                    res.status(400);
                    return gson.toJson("Dados do prestador inválidos");
                }
                res.status(201);
                return gson.toJson(criado);
            });

            put("/:id", (req, res) -> {
                res.type("application/json");
                int id = Integer.parseInt(req.params(":id"));
                Prestador prestadorAtualizado = gson.fromJson(req.body(), Prestador.class);

                // Gerar hash MD5 da senha
                String senhaHash = HashUtil.hashMD5(prestadorAtualizado.getSenha());
                prestadorAtualizado.setSenha(senhaHash);

                boolean sucesso = prestadorService.atualizarPrestador(prestadorAtualizado, id);
                if (!sucesso) {
                    res.status(400);
                    return gson.toJson("Falha ao atualizar prestador");
                }
                res.status(200);
                return gson.toJson(prestadorAtualizado);
            });

            delete("/:id", (req, res) -> {
                res.type("application/json");
                int id = Integer.parseInt(req.params(":id"));
                boolean sucesso = prestadorService.deletarPrestador(id);
                if (!sucesso) {
                    res.status(404);
                    return gson.toJson("Prestador não encontrado");
                }
                res.status(204);
                return "";
            });
        });

        // Rotas Pedido (/pedido)
        path("/pedido", () -> {
            PedidoService pedidoService = new PedidoService();

            get("", (req, res) -> {
                res.type("application/json");
                var pedidos = pedidoService.pegarTodosOsPedidos();
                res.status(200);
                return gson.toJson(pedidos);
            });

            get("/:id", (req, res) -> {
                try {
                    int id = Integer.parseInt(req.params(":id"));
                    Pedido pedido = pedidoService.pegarPedidoId(id);
                    if (pedido == null) {
                        res.status(404);
                        return gson.toJson("Pedido não encontrado");
                    }
                    res.status(200);
                    return gson.toJson(pedido);
                } catch (NumberFormatException e) {
                    res.status(400);
                    return gson.toJson("ID inválido, deve ser número");
                } catch (IllegalArgumentException e) {
                    res.status(400);
                    return gson.toJson(e.getMessage());
                } catch (Exception e) {
                    res.status(500);
                    return gson.toJson("Erro interno: " + e.getMessage());
                }
            });

            post("", (req, res) -> {
                res.type("application/json");
                Pedido novoPedido = gson.fromJson(req.body(), Pedido.class);
                Pedido criado = pedidoService.criaPedido(novoPedido);
                if (criado == null) {
                    res.status(400);
                    return gson.toJson("Dados do pedido inválidos");
                }
                res.status(201);
                return gson.toJson(criado);
            });

            put("/:id", (req, res) -> {
                res.type("application/json");
                int id = Integer.parseInt(req.params(":id"));
                // Detecta se o body tem só o campo status
                Map<String, Object> bodyMap = gson.fromJson(req.body(), Map.class);
                if (bodyMap != null && bodyMap.size() == 1 && bodyMap.containsKey("status")) {
                    String status = (String) bodyMap.get("status");
                    boolean sucesso = pedidoService.atualizarStatusPedido(id, status);
                    if (!sucesso) {
                        res.status(400);
                        return gson.toJson("Falha ao atualizar status do pedido");
                    }
                    res.status(200);
                    return gson.toJson(Map.of("status", status));
                } else {
                Pedido pedidoAtualizado = gson.fromJson(req.body(), Pedido.class);
                boolean sucesso = pedidoService.atualizarPedido(pedidoAtualizado, id);
                if (!sucesso) {
                    res.status(400);
                    return gson.toJson("Falha ao atualizar pedido");
                }
                res.status(200);
                return gson.toJson(pedidoAtualizado);
                }
            });

            delete("/:id", (req, res) -> {
                res.type("application/json");
                int id = Integer.parseInt(req.params(":id"));
                boolean sucesso = pedidoService.deletarPedido(id);
                if (!sucesso) {
                    res.status(404);
                    return gson.toJson("Pedido não encontrado");
                }
                res.status(204);
                return "";
            });
        });

        // Tratamento global de exceções
        exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.type("application/json");
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erro interno no servidor");
            error.put("error", exception.getMessage());
            response.body(gson.toJson(error));
        });
    }
}
