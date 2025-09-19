import javax.swing.*
import java.awt.*

class BancoAppSwing : JFrame("Banco Interactivo") {
    private var cuenta: Cuenta? = null

    private val saldoInicialInput = JTextField(10)
    private val tasaInput = JTextField(10)
    private val tipoCuenta = JComboBox(arrayOf("Cuenta de Ahorros", "Cuenta Corriente"))
    private val crearCuentaBtn = JButton("Crear cuenta")
    private val nuevaCuentaBtn = JButton("Nueva cuenta")

    private val montoInput = JTextField(10)
    private val consignarBtn = JButton("Consignar")
    private val retirarBtn = JButton("Retirar")
    private val interesBtn = JButton("Calcular interés mensual")
    private val extractoBtn = JButton("Generar extracto mensual")
    private val imprimirBtn = JButton("Mostrar información")

    private val logArea = JTextArea(12, 30)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()

        // Panel superior: creación de cuenta
        val topPanel = JPanel()
        topPanel.add(JLabel("Saldo inicial:"))
        topPanel.add(saldoInicialInput)
        topPanel.add(JLabel("Tasa anual:"))
        topPanel.add(tasaInput)
        topPanel.add(tipoCuenta)
        topPanel.add(crearCuentaBtn)
        topPanel.add(nuevaCuentaBtn)
        nuevaCuentaBtn.isEnabled = false // Inicialmente deshabilitado

        // Panel central: operaciones
        val centerPanel = JPanel()
        centerPanel.layout = GridLayout(3, 2, 5, 5)
        centerPanel.add(JLabel("Monto:"))
        centerPanel.add(montoInput)
        centerPanel.add(consignarBtn)
        centerPanel.add(retirarBtn)
        centerPanel.add(interesBtn)
        centerPanel.add(extractoBtn)

        // Panel inferior: log
        val bottomPanel = JPanel()
        bottomPanel.layout = BorderLayout()
        logArea.isEditable = false
        bottomPanel.add(JScrollPane(logArea), BorderLayout.CENTER)
        bottomPanel.add(imprimirBtn, BorderLayout.SOUTH)

        // Agregar a la ventana
        add(topPanel, BorderLayout.NORTH)
        add(centerPanel, BorderLayout.CENTER)
        add(bottomPanel, BorderLayout.SOUTH)

        pack()
        setLocationRelativeTo(null)
        isVisible = true

        // Eventos
        crearCuentaBtn.addActionListener {
            val saldo = saldoInicialInput.text.toFloatOrNull() ?: 0f
            val tasa = tasaInput.text.toFloatOrNull() ?: 0f
            cuenta = when (tipoCuenta.selectedItem) {
                "Cuenta de Ahorros" -> CuentaAhorros(saldo, tasa)
                "Cuenta Corriente" -> CuentaCorriente(saldo, tasa)
                else -> null
            }
            if (cuenta != null) {
                logArea.text = "Cuenta creada: ${tipoCuenta.selectedItem}\n"
                saldoInicialInput.isEnabled = false
                tasaInput.isEnabled = false
                tipoCuenta.isEnabled = false
                crearCuentaBtn.isEnabled = false
                nuevaCuentaBtn.isEnabled = true
            } else {
                logArea.append("Error al crear la cuenta.\n")
            }
        }

        nuevaCuentaBtn.addActionListener {
            saldoInicialInput.text = ""
            tasaInput.text = ""
            tipoCuenta.selectedIndex = 0
            saldoInicialInput.isEnabled = true
            tasaInput.isEnabled = true
            tipoCuenta.isEnabled = true
            crearCuentaBtn.isEnabled = true
            nuevaCuentaBtn.isEnabled = false
            logArea.text = "Ingrese los datos para crear una nueva cuenta.\n"
        }

        consignarBtn.addActionListener {
            val monto = montoInput.text.toFloatOrNull() ?: 0f
            cuenta?.let {
                logArea.append(it.consignar(monto) + "\n")
                montoInput.text = ""
            }
        }

        retirarBtn.addActionListener {
            val monto = montoInput.text.toFloatOrNull() ?: 0f
            cuenta?.let {
                logArea.append(it.retirar(monto) + "\n")
                montoInput.text = ""
            }
        }

        interesBtn.addActionListener {
            cuenta?.calcularInteresMensual()
            logArea.append("Interés mensual calculado.\n")
        }

        extractoBtn.addActionListener {
            cuenta?.extractoMensual()
            logArea.append("Extracto mensual generado.\n")
        }

        imprimirBtn.addActionListener {
            logArea.text = "" // Limpia log
            cuenta?.let {
                logArea.append(it.info() + "\n")
            }
        }
    }
}

fun main() {
    SwingUtilities.invokeLater { BancoAppSwing() }
}
