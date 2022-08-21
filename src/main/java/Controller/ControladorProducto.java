package Controller;
import Model.Producto;
import Model.RepositorioProducto;
import View.View;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;

/**
 *
 * @author Roberth
 */
public class ControladorProducto implements ActionListener {

    RepositorioProducto repositorio;

    View vista;

    DefaultTableModel defaultTableModel;

    public ControladorProducto(RepositorioProducto repositorio, View vista) {
        this.repositorio = repositorio;
        this.vista = vista;
        agregarEventos();
        listar();
    }

    public void agregarEventos() {
        vista.getAdd().addActionListener(this);
        vista.getDelete().addActionListener(this);
        vista.getUpdate().addActionListener(this);
        vista.getReports().addActionListener(this);
        vista.getTableProduct().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            }
        });
    }

    public void  listar(){
        String[] titulos = {"Nombre", "Precio", "Cantidad"};
        defaultTableModel = new DefaultTableModel(titulos, 0);
        List<Producto> productos = (List<Producto>) repositorio.findAll();
        for (Producto producto : productos) {
            defaultTableModel.addRow(new Object[]{producto.getNombre(), producto.getPrecio(), producto.getInventario()});
        }
        vista.getTableProduct().setModel(defaultTableModel);
    }

    public boolean validarCampos(){
        String inputNombre = vista.getIntputName().getText();
        String inputPrecio = vista.getInputPrice().getText();
        String inputInventario = vista.getInputInventory().getText();

        if(inputNombre.isEmpty() || inputPrecio.isEmpty() || inputInventario.isEmpty()){
            return false;
        } else if (Double.parseDouble(inputPrecio) <= 0 || Integer.parseInt(inputInventario) <= 0){
            return false;
        }
        return true;
    };
    public boolean existeProducto(){
        List<Producto> productos = (List<Producto>) repositorio.findAll();
        boolean existe = false;
        for (Producto producto : productos) {
            if (producto.getNombre().toLowerCase().equals(vista.getIntputName().getText().toLowerCase())) {
                existe = true;
            }
        }
        return existe;
    };

    public Producto getProductoByName(){
        List<Producto> productos = (List<Producto>) repositorio.findAll();
        String nombre = vista.getTableProduct().getValueAt(vista.getTableProduct().getSelectedRow(), 0).toString();
        for (Producto producto : productos) {
            if (producto.getNombre().toLowerCase().equals(nombre.toLowerCase())) {
                return producto;
            }
        }
        return null;
    };

    public String nombrePrecioMayor(){
        List<Producto> productos = (List<Producto>) repositorio.findAll();
        String nombre = "";
        Double precio = 0.0;
        for (Producto producto : productos) {
            if (producto.getPrecio() > precio) {
                nombre = producto.getNombre();
                precio = producto.getPrecio();
            }
        }
        return nombre;
    };

    public String nombrePrecioMenor(){
        List<Producto> productos = (List<Producto>) repositorio.findAll();
        String nombre = "";
        Double precio = Double.MAX_VALUE;
        for (Producto producto : productos) {
            if (producto.getPrecio() < precio) {
                nombre = producto.getNombre();
                precio = producto.getPrecio();
            }
        }
        return nombre;
    };

    public float promedioPrecio() {
        List<Producto> productos = (List<Producto>) repositorio.findAll();
        float promedio = 0;
        for (Producto producto : productos) {
            promedio += producto.getPrecio();
        }
        return (float) (Math.round(promedio / productos.size() * 10) / 10.0);
    };

    public double valorInventario(){
        List<Producto> productos = (List<Producto>) repositorio.findAll();
        double valor = 0;
        for (Producto producto : productos) {
            valor += producto.getPrecio() * producto.getInventario();
        }
        return valor;
    };

    public void agregar(){
        if(validarCampos()){
            if(existeProducto()){
                JOptionPane.showMessageDialog(null, "El producto ya existe");
            }else {
                Producto producto = Producto.crearProducto(vista.getIntputName().getText(), Double.parseDouble(vista.getInputPrice().getText()), Integer.parseInt(vista.getInputInventory().getText()));
                repositorio.save(producto);
                JOptionPane.showMessageDialog(null, "El producto se ha agregado correctamente");
            }

        }else {
            JOptionPane.showMessageDialog(null, "Por favor verifique los datos ingresados");
        }

    }

    public void eliminar(){
        if (vista.getTableProduct().getSelectedRow() != -1){
            Producto producto = getProductoByName();
            repositorio.delete(producto);
            JOptionPane.showMessageDialog(null, "El producto se ha eliminado correctamente");

        }else {
            JOptionPane.showMessageDialog(null, "Por favor seleccione un producto");
        }
    };

    public void actualizar(){
        if (vista.getTableProduct().getSelectedRow() != -1){
            String nombre = vista.getTableProduct().getValueAt(vista.getTableProduct().getSelectedRow(), 0).toString();
            String precio = Double.parseDouble(vista.getTableProduct().getValueAt(vista.getTableProduct().getSelectedRow(), 1).toString()) + "";
            String inventario = Integer.parseInt(vista.getTableProduct().getValueAt(vista.getTableProduct().getSelectedRow(), 2).toString()) + "";

            // creamos JFrame para actualizar el producto
            JFrame frame = new JFrame("Actualizar Producto");
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            frame.setResizable(false);
            frame.setLayout(null);

            // creamos un JPannel para el formulario
            JPanel panel = new JPanel();
            panel.setSize(400, 400);
            panel.setLocation(0, 0);
            panel.setLayout(null);
            frame.add(panel);

            // creamos un JLabel para el titulo del formulario
            JLabel label = new JLabel("Actualizar Producto");
            label.setSize(300, 30);
            label.setLocation(150, 10);
            panel.add(label);

            // creamos un JLabel para el nombre
            JLabel labelName = new JLabel("Nombre");
            labelName.setSize(100, 30);
            labelName.setLocation(30, 50);
            panel.add(labelName);

            // creamos un JTextField para el nombre
            JTextField intputName = new JTextField();
            intputName.setSize(150, 20);
            intputName.setLocation(120, 55);
            intputName.setText(nombre);
            panel.add(intputName);

            // creamos un JLabel para el precio
            JLabel labelPrice = new JLabel("Precio");
            labelPrice.setSize(100, 30);
            labelPrice.setLocation(30, 100);
            panel.add(labelPrice);

            // creamos un JTextField para el precio
            JTextField inputPrice = new JTextField();
            inputPrice.setSize(150, 20);
            inputPrice.setLocation(120, 105);
            inputPrice.setText(String.valueOf(precio));
            panel.add(inputPrice);

            // creamos un JLabel para el inventario
            JLabel labelInventory = new JLabel("Inventario");
            labelInventory.setSize(100, 30);
            labelInventory.setLocation(30, 150);
            panel.add(labelInventory);

            // creamos un JTextField para el inventario
            JTextField inputInventory = new JTextField();
            inputInventory.setSize(150, 20);
            inputInventory.setLocation(120, 155);
            inputInventory.setText(String.valueOf(inventario));
            panel.add(inputInventory);

            // creamos un boton para actualizar el producto
            JButton button = new JButton("Actualizar");
            button.setSize(120, 30);
            button.setLocation(150, 200);
            panel.add(button);

            String nameUpdate = intputName.getText();
            // cuando se haga click en el boton se ejecuta se imprime hola
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(intputName.getText().equals("") || inputPrice.getText().equals("") || inputInventory.getText().equals("") && Double.parseDouble(inputPrice.getText()) <= 0 || Integer.parseInt(inputInventory.getText()) <= 0){
                        JOptionPane.showMessageDialog(null, "Por favor verifique los datos ingresados");
                    }else {
                        List<Producto> productos = (List<Producto>) repositorio.findAll();
                        for (Producto producto : productos) {
                            if (producto.getNombre().equals(nameUpdate)) {
                                producto.setNombre(intputName.getText());
                                producto.setPrecio(Double.parseDouble(inputPrice.getText()));
                                producto.setInventario(Integer.parseInt(inputInventory.getText()));
                                repositorio.save(producto);
                                JOptionPane.showMessageDialog(null, "El producto se ha actualizado correctamente");
                                frame.dispose();
                                listar();
                            }
                        }

                    }
                }
            });

        }else {
            JOptionPane.showMessageDialog(null, "Por favor seleccione un producto");
        }
    };

    public void informe(){
        // creamos JFrame para mostrar el informe
        JFrame frame = new JFrame("Informe");
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLayout(null);

        // creamos un JPannel para la informacion
        JPanel panel = new JPanel();
        panel.setSize(500, 300);
        panel.setLocation(0, 0);
        panel.setLayout(null);
        frame.add(panel);

        // creamos JLabel para producto precio mayor
        JLabel label = new JLabel("Producto con mayor precio: ");
        label.setSize(300, 30);
        label.setLocation(100, 20);
        panel.add(label);

        // nombre del producto con mayor precio
        JLabel labelName = new JLabel(nombrePrecioMayor());
        labelName.setSize(300, 30);
        labelName.setLocation(307, 20);
        panel.add(labelName);

        // creamos JLabel para producto precio menor
        JLabel label2 = new JLabel("Producto con menor precio: ");
        label2.setSize(300, 30);
        label2.setLocation(100, 60);
        panel.add(label2);

        // nombre del producto con menor precio
        JLabel labelName2 = new JLabel(nombrePrecioMenor());
        labelName2.setSize(300, 30);
        labelName2.setLocation(307, 60);
        panel.add(labelName2);

        // creamos JLabel Promedio de precios
        JLabel label3 = new JLabel("Promedio de precios: ");
        label3.setSize(300, 30);
        label3.setLocation(100, 100);
        panel.add(label3);

        // promedio de precios
        JLabel labelName3 = new JLabel(String.valueOf(promedioPrecio()));
        labelName3.setSize(300, 30);
        labelName3.setLocation(260, 100);
        panel.add(labelName3);

        // creamos JLabel para valor del inventario
        JLabel label4 = new JLabel("Valor del inventario: ");
        label4.setSize(300, 30);
        label4.setLocation(100, 140);
        panel.add(label4);

        // valor del inventario
        JLabel labelName4 = new JLabel(String.valueOf(valorInventario()));
        labelName4.setSize(300, 30);
        labelName4.setLocation(260, 140);
        panel.add(labelName4);
    };


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getAdd()) {
            agregar();
            listar();
        } else if (e.getSource() == vista.getDelete()) {
            eliminar();
            listar();
        } else if (e.getSource() == vista.getUpdate()) {
            actualizar();
        } else if (e.getSource() == vista.getReports()) {
            informe();
        }
    }
}
