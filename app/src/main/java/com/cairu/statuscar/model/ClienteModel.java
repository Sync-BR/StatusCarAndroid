package com.cairu.statuscar.model;

public class ClienteModel {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String endereco;
    private String login;
    private String senha;

    public ClienteModel() {
    }

//    @Override
//    public String toString() {
//        return "ClienteModel{" +
//                "id=" + id +
//                ", nome='" + nome + '\'' +
//                ", cpf='" + cpf + '\'' +
//                ", telefone='" + telefone + '\'' +
//                ", email='" + email + '\'' +
//                ", endereco='" + endereco + '\'' +
//                ", login='" + login + '\'' +
//                ", senha='" + senha + '\'' +
//                '}';
//    }
@Override
public String toString() {
    return nome; // Retorna apenas o nome do cliente
}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
