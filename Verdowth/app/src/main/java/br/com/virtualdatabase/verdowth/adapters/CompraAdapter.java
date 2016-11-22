package br.com.virtualdatabase.verdowth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.virtualdatabase.verdowth.Compra;
import br.com.virtualdatabase.verdowth.R;

public class CompraAdapter extends ArrayAdapter<Compra> {


    public CompraAdapter(Context context, int resource, ArrayList<Compra> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.linha_lista_cesta_de_compras, null);
        }

        Compra c = getItem(position);

        if (c != null) {
            ImageView imageProduct = (ImageView) v.findViewById(R.id.imageProductInList);
            TextView nameProduct = (TextView) v.findViewById(R.id.nameProductInList);
            TextView quantityProduct = (TextView) v.findViewById(R.id.quantityProduct);
            TextView priceProduct = (TextView) v.findViewById(R.id.unitaryPrice);
            TextView priceTotalLinha = (TextView) v.findViewById(R.id.priceProductInList);

            if (imageProduct != null) {
                switch (c.getName()){
                    case "Morango":
                        imageProduct.setImageResource(R.drawable.morango_48);
                        break;
                    case "Tomate":
                        imageProduct.setImageResource(R.drawable.tomato_48);
                        break;
                    case "Pera":
                        imageProduct.setImageResource(R.drawable.pear_48);
                        break;

                }
            }


            if (nameProduct != null) {
                nameProduct.setText(c.getName());
            }


            if (priceProduct != null) {
                priceProduct.setText("R$ "+ String.valueOf(c.getUnitaryPrice()));
            }

            if (quantityProduct != null) {
                quantityProduct.setText(String.valueOf(c.getQuantity()));
            }


            if (priceTotalLinha != null) {
              //  priceProduct.setText("R$ "+ String.valueOf(c.getQuantity()*c.getUnitaryPrice()));
                  priceProduct.setText("R$ "+ String.valueOf(c.getQuantity()*c.getUnitaryPrice()));

            }

        }

        return v;
    }


}