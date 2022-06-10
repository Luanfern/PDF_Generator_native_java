package com.example.pdf_javaand;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.RestrictionsManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button gerarPDF;
    EditText titulo, conteudo, nomeDoArquivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gerarPDF = findViewById(R.id.gerarpdf);
        titulo = findViewById(R.id.titulo);
        conteudo = findViewById(R.id.texto);
        nomeDoArquivo = findViewById(R.id.archiveName);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        createPDF();
    }

    private void createPDF(){
        gerarPDF.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (titulo.getText().toString().length() == 0 ||
                        conteudo.getText().toString().length() == 0 ||
                        nomeDoArquivo.getText().toString().length() == 0){
                    Toast.makeText(MainActivity.this, "Complete os campos", Toast.LENGTH_LONG).show();
                }else{

                    File pathDir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
                    /*File pathDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+File.separator+"PDFs-Gerados");
                    if (pathDir.exists() == false){
                        File newdir = new File(pathDir.getAbsolutePath());
                        newdir.mkdirs();
                        System.out.println("Path Criado!");
                    } else{
                        System.out.println("Path já criado!");
                    }*/

                    Toast.makeText(MainActivity.this, "Gerando PDF...", Toast.LENGTH_LONG).show();

                    PdfDocument myPdfDocument = new PdfDocument();
                    Paint myPaint = new Paint();
                    Paint titlePaint = new Paint();

                    PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                    PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
                    Canvas canvas = myPage1.getCanvas();

                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(50);
                    canvas.drawText(String.valueOf(titulo.getText()), 600, 170, titlePaint);

                    titlePaint.setTextAlign(Paint.Align.LEFT);
                    titlePaint.setTextSize(25);
                    canvas.drawText(String.valueOf(conteudo.getText()), 300, 280, titlePaint);




                    myPdfDocument.finishPage(myPage1);

                    String arquivo = String.valueOf(nomeDoArquivo.getText())+".pdf";

                    try {
                        File downloadFolder = new File(pathDir.getAbsolutePath(), arquivo);
                        System.out.println("PDF Criado!");
                        System.out.println(downloadFolder.getAbsolutePath());
                        myPdfDocument.writeTo(new FileOutputStream(downloadFolder));
                        Toast.makeText(MainActivity.this, "PDF Com conteudo", Toast.LENGTH_LONG).show();
                        System.out.println("PDF Com conteudo!");
                    } catch (FileNotFoundException e) {
                        System.out.println(e);
                        Toast.makeText(MainActivity.this, "Erro ao gerar PDF", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.out.println(e);
                        Toast.makeText(MainActivity.this, "Erro ao gerar PDF", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}