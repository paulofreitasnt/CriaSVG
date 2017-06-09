package com.ifpb.relacionamentos.controle;

import com.ifpb.relacionamentos.banco.ConFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author paulo
 */
public class CriaSvg {

    private final File modelo;
    private final File arquivo;
    private final Connection con;

    public CriaSvg() throws IOException, ClassNotFoundException, SQLException {

        con = new ConFactory().getConnection();
        modelo = new File("Modelo.svg");
        arquivo = new File("Saida.svg");

        if (!arquivo.exists()) {
            arquivo.createNewFile();
        }
    }

    public boolean cidadeExiste(String cidade) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM municipio"
                + " WHERE nome ilike ?");
        stmt.setString(1, cidade);

        return stmt.executeQuery().next();

    }

    public void criaArquivo(String cidade) throws FileNotFoundException, IOException, SQLException {

        FileReader fileReader = new FileReader(modelo);
        BufferedReader reader = new BufferedReader(fileReader);

        FileWriter fw = new FileWriter(arquivo, false);
        PrintWriter pw = new PrintWriter(fw);

        copiarNLinhas(pw, reader, 3);

        PreparedStatement stmt = con.prepareStatement("SELECT getTamanhoViewBox(?)");
        stmt.setString(1, cidade);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            pw.write(rs.getString(1));
        }

        copiarNLinhas(pw, reader, 2);

        stmt = con.prepareStatement("SELECT ST_AsSVG(geom) FROM municipio WHERE nome ilike ?");
        stmt.setString(1, cidade);
        rs = stmt.executeQuery();

        while (rs.next()) {
            pw.write(rs.getString(1));
        }

        copiarNLinhas(pw, reader, 2);

        reader.close();
        pw.close();

    }

    private void copiarNLinhas(PrintWriter pw, BufferedReader reader, int quant) throws IOException {
        for (int i = 0; i < quant; i++) {
            pw.println(reader.readLine());
        }
    }

}
