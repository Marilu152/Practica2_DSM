
class CuentaCorriente(saldoInicial: Float, tasaAnual: Float) : Cuenta(saldoInicial, tasaAnual) {
    private var sobregiro: Float = 0f

    override fun retirar(cantidad: Float): String {
        return if (cantidad <= saldo) {
            saldo -= cantidad
            numeroRetiros++
            "Se retiró $$cantidad correctamente."
        } else {
            val exceso = cantidad - saldo
            saldo = 0f
            sobregiro += exceso
            numeroRetiros++
            "Se retiró $$cantidad, generando sobregiro de $$sobregiro."
        }
    }

    override fun consignar(cantidad: Float): String {
        return if (sobregiro > 0) {
            if (cantidad >= sobregiro) {
                val restante = cantidad - sobregiro
                sobregiro = 0f
                saldo += restante
                numeroConsignaciones++
                "Se consignó $$cantidad, pagando sobregiro. Saldo restante: $$saldo"
            } else {
                sobregiro -= cantidad
                numeroConsignaciones++
                "Se consignó $$cantidad, reduciendo sobregiro a $$sobregiro"
            }
        } else super.consignar(cantidad)
    }

    override fun info(): String {
        return super.info() + "\nSobregiro: $$sobregiro"
    }
}
