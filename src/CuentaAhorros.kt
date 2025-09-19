class CuentaAhorros(saldoInicial: Float, tasaAnual: Float) : Cuenta(saldoInicial, tasaAnual) {
    private var activa: Boolean = saldo >= 10000

    override fun consignar(cantidad: Float): String {
        return if (activa) super.consignar(cantidad)
        else "Cuenta inactiva, no se puede consignar."
    }

    override fun retirar(cantidad: Float): String {
        return if (activa) {
            if (cantidad <= saldo) super.retirar(cantidad)
            else "Fondos insuficientes para retirar."
        } else "Cuenta inactiva, no se puede retirar."
    }

    override fun extractoMensual() {
        if (numeroRetiros > 4) {
            comisionMensual += (numeroRetiros - 4) * 1000
        }
        super.extractoMensual()
        activa = saldo >= 10000
    }

    override fun info(): String {
        return super.info() + "\nCuenta activa: $activa"
    }
}
