package jogoJava;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

public class main {

    static class Jogador {
        String nome;
        int pontuacao;

        public Jogador(String nome, int pontuacao) {
            this.nome = nome;
            this.pontuacao = pontuacao;
        }
    }

    static class Pergunta {
        String enunciado;
        String[] alternativas;
        int respostaCorreta;

        public Pergunta(String enunciado, String[] alternativas, int respostaCorreta) {
            this.enunciado = enunciado;
            this.alternativas = alternativas;
            this.respostaCorreta = respostaCorreta;
        }
    }

    private static final List<Jogador> ranking = new ArrayList<>();
    private static final Random random = new Random();
    private static boolean cancelado = false;

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Bem-vindo ao Desafio Arcade!");

        boolean querJogar = true;

        while (querJogar && !cancelado) {
            executarPartida();

            if (cancelado) {
                break;
            }

            int resposta = JOptionPane.showConfirmDialog(
                    null, "Quer jogar novamente?", "Desafio Arcade",
                    JOptionPane.YES_NO_OPTION
            );

            exibirRanking();
            querJogar = resposta == JOptionPane.YES_OPTION;
        }

        if (!cancelado) {
            JOptionPane.showMessageDialog(null, "Obrigado por jogar!");
        }
    }

    private static void executarPartida() {
        String nomeJogador = JOptionPane.showInputDialog(null, "Digite seu nome:");

        if (nomeJogador == null) {
            cancelado = true;
            return;
        }

        nomeJogador = nomeJogador.trim();
        if (nomeJogador.isEmpty()) {
            nomeJogador = "Jogador Anônimo";
        }

        int pontuacaoTotal = 0;
        int vitoriasJogador = 0;
        int vitoriasComputador = 0;
        String[] opcoes = {"Pedra", "Papel", "Tesoura"};

        JOptionPane.showMessageDialog(null,
                "Fase 1: Pedra, Papel e Tesoura\nVença 2 rodadas para passar.\nCada vitória vale 15 pontos.");

        for (int rodada = 1; rodada <= 3; rodada++) {
            int escolhaJogador = JOptionPane.showOptionDialog(
                    null, "Rodada " + rodada + " de 3. Escolha:",
                    "Fase 1", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]
            );

            if (escolhaJogador == -1) {
                cancelado = true;
                return;
            }

            int escolhaComputador = random.nextInt(3);
            String resultado;

            if (escolhaJogador == escolhaComputador) {
                resultado = "Empate!";
            } else if ((escolhaJogador == 0 && escolhaComputador == 2)
                    || (escolhaJogador == 1 && escolhaComputador == 0)
                    || (escolhaJogador == 2 && escolhaComputador == 1)) {
                resultado = "Você venceu! +15 pontos";
                vitoriasJogador++;
                pontuacaoTotal += 15;
            } else {
                resultado = "O computador venceu.";
                vitoriasComputador++;
            }

            JOptionPane.showMessageDialog(null,
                    "Você: " + opcoes[escolhaJogador]
                    + "\nComputador: " + opcoes[escolhaComputador]
                    + "\n\n" + resultado
                    + "\nPlacar: " + vitoriasJogador + " x " + vitoriasComputador);

            if (vitoriasJogador == 2 || vitoriasComputador == 2) {
                break;
            }
        }

        if (vitoriasJogador < 2) {
            JOptionPane.showMessageDialog(null, "Você não passou da fase 1.");
            finalizarPartida(nomeJogador, pontuacaoTotal);
            return;
        }

        JOptionPane.showMessageDialog(null, "Boa! Você passou para a fase 2.\nPontos: " + pontuacaoTotal);

        int pontuacaoFase2 = aplicarQuiz(obterPerguntasJava(), "Fase 2: Quiz de Java");
        if (cancelado) {
            return;
        }

        pontuacaoTotal += pontuacaoFase2;
        if (pontuacaoFase2 < 60) {
            JOptionPane.showMessageDialog(null, "Você fez " + pontuacaoFase2
                    + " pontos e não alcançou os 60 necessários.");
            finalizarPartida(nomeJogador, pontuacaoTotal);
            return;
        }

        JOptionPane.showMessageDialog(null, "Mandou bem! Você chegou à fase final.\nPontos: " + pontuacaoTotal);

        int pontuacaoFase3 = aplicarQuiz(obterPerguntasCulturaPop(), "Fase 3: Conhecimentos gerais");
        if (cancelado) {
            return;
        }

        pontuacaoTotal += pontuacaoFase3;
        JOptionPane.showMessageDialog(null, "Você terminou o jogo!\nPontos na fase 3: " + pontuacaoFase3);
        finalizarPartida(nomeJogador, pontuacaoTotal);
    }

    private static int aplicarQuiz(List<Pergunta> perguntas, String titulo) {
        int pontos = 0;

        for (int i = 0; i < perguntas.size(); i++) {
            Pergunta pergunta = perguntas.get(i);
            int resposta = JOptionPane.showOptionDialog(
                    null, (i + 1) + "/" + perguntas.size() + " - " + pergunta.enunciado,
                    titulo, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, pergunta.alternativas, pergunta.alternativas[0]
            );

            if (resposta == -1) {
                cancelado = true;
                return pontos;
            }

            if (resposta + 1 == pergunta.respostaCorreta) {
                pontos += 15;
                JOptionPane.showMessageDialog(null, "Acertou! +15 pontos.");
            } else {
                JOptionPane.showMessageDialog(null, "Não foi dessa vez. A resposta era: "
                        + pergunta.alternativas[pergunta.respostaCorreta - 1]);
            }
        }

        JOptionPane.showMessageDialog(null, "Você fez " + pontos + " de "
                + (perguntas.size() * 15) + " pontos.");
        return pontos;
    }

    private static void finalizarPartida(String nome, int pontuacao) {
        ranking.add(new Jogador(nome, pontuacao));
        JOptionPane.showMessageDialog(null,
                "Fim de jogo!\nJogador: " + nome + "\nPontuação: " + pontuacao);
    }

    private static void exibirRanking() {
        if (ranking.isEmpty()) {
            JOptionPane.showMessageDialog(null, "O ranking está vazio.");
            return;
        }

        Collections.sort(ranking, new Comparator<Jogador>() {
            @Override
            public int compare(Jogador j1, Jogador j2) {
                return Integer.compare(j2.pontuacao, j1.pontuacao);
            }
        });

        StringBuilder texto = new StringBuilder("Ranking\n\n");
        for (int i = 0; i < ranking.size(); i++) {
            Jogador jogador = ranking.get(i);
            texto.append(i + 1).append("º - ").append(jogador.nome)
                    .append(": ").append(jogador.pontuacao).append(" pontos\n");
        }

        JOptionPane.showMessageDialog(null, texto.toString(), "Ranking", JOptionPane.INFORMATION_MESSAGE);
    }

    private static List<Pergunta> obterPerguntasJava() {
        List<Pergunta> lista = new ArrayList<>();
        lista.add(new Pergunta(
                "Qual tipo guarda números inteiros?",
                new String[]{"float", "int", "boolean"}, 2));
        lista.add(new Pergunta(
                "Qual repetição executa ao menos uma vez antes de testar a condição?",
                new String[]{"for", "while", "do-while"}, 3));
        lista.add(new Pergunta(
                "O que static indica em um método?",
                new String[]{"Pertence à classe", "É privado", "Não pode ser alterado"}, 1));
        lista.add(new Pergunta(
                "Qual opção é uma palavra reservada do Java?",
                new String[]{"function", "class", "define"}, 2));
        lista.add(new Pergunta(
                "Como obter o tamanho de um array chamado arr?",
                new String[]{"arr.length()", "arr.size()", "arr.length"}, 3));
        return lista;
    }

    private static List<Pergunta> obterPerguntasCulturaPop() {
        List<Pergunta> lista = new ArrayList<>();
        lista.add(new Pergunta(
                "Qual planeta fica mais perto do Sol?",
                new String[]{"Vênus", "Mercúrio", "Marte"}, 2));
        lista.add(new Pergunta(
                "Em que ano o homem pisou na Lua?",
                new String[]{"1969", "1972", "1965"}, 1));
        lista.add(new Pergunta(
                "Qual linguagem é usada para estilizar páginas web?",
                new String[]{"HTML", "JavaScript", "CSS"}, 3));
        lista.add(new Pergunta(
                "Qual bloco é usado para construir um portal do Nether?",
                new String[]{"Bedrock", "Obsidiana", "Pedregulho"}, 2));
        lista.add(new Pergunta(
                "Qual empresa desenvolveu o Android?",
                new String[]{"Google", "Apple", "Microsoft"}, 1));
        lista.add(new Pergunta(
                "Quem é conhecido como o Amigão da Vizinhança?",
                new String[]{"Batman", "Homem de Ferro", "Homem-Aranha"}, 3));
        lista.add(new Pergunta(
                "Qual elemento tem o símbolo Au?",
                new String[]{"Prata", "Ouro", "Alumínio"}, 2));
        lista.add(new Pergunta(
                "Qual é o maior oceano da Terra?",
                new String[]{"Pacífico", "Atlântico", "Índico"}, 1));
        lista.add(new Pergunta(
                "Qual console a Sony lançou em 1994?",
                new String[]{"Nintendo 64", "PlayStation", "Sega Saturn"}, 2));
        lista.add(new Pergunta(
                "O que significa SQL?",
                new String[]{"Structured Query Language", "Simple Quality Logic", "System Quantum Loop"}, 1));
        return lista;
    }
}
