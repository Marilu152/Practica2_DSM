// Clase base
open class Cuenta(
    protected var saldo: Float,
    protected var tasaAnual: Float
) {
    protected var numeroConsignaciones: Int = 0
    protected var numeroRetiros: Int = 0
    protected var comisionMensual: Float = 0f

    open fun consignar(cantidad: Float): String {
        return if (cantidad > 0) {
            saldo += cantidad
            numeroConsignaciones++
            "Se consignó $$cantidad correctamente."
        } else {
            "Cantidad inválida."
        }
    }

    open fun retirar(cantidad: Float): String {
        return if (cantidad > 0 && cantidad <= saldo) {
            saldo -= cantidad
            numeroRetiros++
            "Se retiró $$cantidad correctamente."
        } else {
            "Fondos insuficientes para retirar $$cantidad."
        }
    }

    fun calcularInteresMensual() {
        val interesMensual = (tasaAnual / 12) / 100 * saldo
        saldo += interesMensual
    }

    open fun extractoMensual() {
        saldo -= comisionMensual
        calcularInteresMensual()
        comisionMensual = 0f
    }

    open fun info(): String {
        return """
            Saldo: $$saldo
            Consignaciones: $numeroConsignaciones
            Retiros: $numeroRetiros
            Tasa anual: $tasaAnual%
            Comisión mensual: $$comisionMensual
        """.trimIndent()
    }
}
