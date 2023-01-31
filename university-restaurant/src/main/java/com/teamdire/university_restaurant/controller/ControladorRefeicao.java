package com.teamdire.university_restaurant.controller;

import com.teamdire.university_restaurant.model.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.stream.Collectors;

public class ControladorRefeicao {
    public ControladorRefeicao() {
    }

    public boolean inserirNovaRefeicao(byte tipoRefeicao, Calendar dataServentia, int quantidadeTiquetes,
                                       String nomeRefeicao, String pratoPrincipal, ArrayList<String> alimentosBase, String guarnicao,
                                       String salada, String bebida, String sobremesa)
    {
        Refeicao refeicao = new Refeicao(tipoRefeicao, dataServentia, quantidadeTiquetes,
                                         nomeRefeicao, pratoPrincipal, alimentosBase, guarnicao, salada, bebida, sobremesa);
        UniversityRestaurant.addRefeicao(refeicao);
        // Persistindo os dados
        UniversityRestaurant.salvar();
        return true;
    }

    public void adicionarCreditos(String cpfBeneficiado, Float valorInserido) {
        // Preparando os dados
        Usuario usuario = UniversityRestaurant.recuperarUsuario(cpfBeneficiado);
        Usuario usuarioResponsavel = UniversityRestaurant.getUsuarioAutenticado();
        Calendar dataInsercao = Calendar.getInstance();
        // Adicionando os crédtios
        assert usuario != null;
        usuario.setSaldo(usuario.getSaldo() + valorInserido);
        // Adicionando a transação no log
        LogCreditos log = new LogCreditos(usuario, usuarioResponsavel, valorInserido, dataInsercao);
        UniversityRestaurant.addLogCreditos(log);
        // Persistindo os dados
        UniversityRestaurant.salvar();
    }

    public Float buscarSaldo(String cpf) {
        return Objects.requireNonNull(UniversityRestaurant.recuperarUsuario(cpf)).getSaldo();
    }

    public ArrayList<Refeicao> getRefeicoesSemanaAtual() {
        ArrayList<Refeicao> refeicoes = UniversityRestaurant.getRefeicoes();
        Calendar dataAtual = Calendar.getInstance();
        // Create a new array, filtering out the refeicoes that are not from the current week
        return refeicoes.stream()
                .filter(refeicao -> refeicao.getDataServentia().get(Calendar.WEEK_OF_YEAR) == dataAtual.get(Calendar.WEEK_OF_YEAR))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Refeicao> getRefeicoesSemanaSeguinte() {
        ArrayList<Refeicao> refeicoes = UniversityRestaurant.getRefeicoes();
        Calendar dataAtual = Calendar.getInstance();
        // Create a new array, filtering out the refeicoes that are not from the next week
        return refeicoes.stream()
                .filter(refeicao -> refeicao.getDataServentia().get(Calendar.WEEK_OF_YEAR) == dataAtual.get(Calendar.WEEK_OF_YEAR) + 1)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}