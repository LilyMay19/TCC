/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import dao.PacienteDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Paciente;

/**
 *
 * @author LUANA
 */
@WebServlet(name = "PacienteWS", urlPatterns = {"/admin/paciente/PacienteWS"})
public class PacienteWS extends HttpServlet {
    
    
    
     @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao= request.getParameter("txtAcao");
        RequestDispatcher destino;
        String pagina;
        String msg;
        PacienteDAO dao= new PacienteDAO();
        Paciente obj;
        List<Paciente> pacientes;
        Boolean deucerto;
        
        switch(String.valueOf(acao)){
            case "add":
                //Abrir a tela
                pagina= "add.jsp";
                
                break;
            case "edit":
                //Abrir a tela e buscar os dados
                obj= dao.buscarPorChavePrimaria(Long.parseLong(request.getParameter("txtId")));
                request.setAttribute("obj", obj);
                 pagina= "edit.jsp";
                 
                break;
            case "del":
                //excluir os dados e buscar dados
                obj= dao.buscarPorChavePrimaria(Long.parseLong(request.getParameter("txtId")));
                deucerto= dao.excluir(obj);
                if(deucerto){
                    msg= obj.getNome() + " deletado com sucesso";
                }
                else{
                    msg= "Problemas ao excluir o paciente" + obj.getNome();
                }
                pacientes= dao.listar();
                request.setAttribute("msg", msg);
                request.setAttribute("lista", pacientes);
                pagina= "list.jsp";
                break;
            default:
                //listar  ou listar com filtro
                String filtro= request.getParameter("txtFiltro");
                if(filtro==null){
                    //lista todos
                    pacientes = dao.listar();
                }
                else{
                    //lista com filtro
            try {
                pacientes= dao.listar(filtro);
            } catch (Exception ex) {
                pacientes= dao.listar();
                 msg= "Problema ao filtrar";
                 request.setAttribute("msg", msg);
            }
                }
                request.setAttribute("lista", pacientes);
                pagina="list.jsp";
                break;
        }
        destino= request.getRequestDispatcher(pagina);
        destino.forward(request, response);
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // criar variável
        Paciente obj;
        PacienteDAO dao = new PacienteDAO();
        String msg;
        Boolean deucerto;
        String pagina;
        RequestDispatcher destino;
        List<Paciente> pacientes;
        
        // Receber dados
        String id = request.getParameter("txtId");
        String nome= request.getParameter("txtNome");
        String email = request.getParameter("txtEmail");
        String datadenascimento = request.getParameter("txtDataNascimento");
        String telefone = request.getParameter("txtTelefone");
        
        // Tratar os dados (transformar os dados no formato solicitado)
        
        if(id!=null){
            //busca o que existe
            obj= dao.buscarPorChavePrimaria(Long.parseLong(id));
        }
        else{
            //cria um novo
            obj= new Paciente();
        }
        
        //adicionar os dados recebidos
        obj.setNome(nome);
        obj.setEmail(email);
       // obj.setDatadenascimento(new java.sql.Date(datadenascimento));
        obj.setTelefone(Long.parseLong(telefone));
        
        if(id !=null){
            deucerto = dao.alterar(obj);
            pagina="list.jsp";
            pacientes = dao.listar();
            request.setAttribute("lista", pacientes);
            if(deucerto){
                msg= obj.getNome() + " alterado com sucesso!";
            }
            else{
                msg= "Problema ao editar o paciente" + obj.getNome();
            }
        }else{
                 deucerto= dao.incluir(obj);
                 pagina="add.jsp";
                    if(deucerto){
                        msg= obj.getNome() + " adicionado com sucesso";
                    }
                    else{
                        msg= "Problemas ao adicionar paciente" + obj.getNome();
                    }
                }
           request.setAttribute("msg", msg);
           destino=request.getRequestDispatcher(pagina);
           destino.forward(request, response);
        }
        
       
    }