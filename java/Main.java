// Clase base Cuenta
class Cuenta {
    protected float saldo;
    protected int numeroConsignaciones = 0;
    protected int numeroRetiros = 0;
    protected float tasaAnual;
    protected float comisionMensual = 0;

    // Constructor
    public Cuenta(float saldoInicial, float tasaAnual) {
        this.saldo = saldoInicial;
        this.tasaAnual = tasaAnual;
    }

    // Método para consignar dinero
    public void consignar(float cantidad) {
        if (cantidad > 0) {
            saldo += cantidad;
            numeroConsignaciones++;
        }
    }

    // Método para retirar dinero
    public void retirar(float cantidad) {
        if (cantidad > 0 && cantidad <= saldo) {
            saldo -= cantidad;
            numeroRetiros++;
        } else {
            System.out.println("Fondos insuficientes para retirar: $" + cantidad);
        }
    }

    // Método para calcular interés mensual
    public void calcularInteresMensual() {
        float interesMensual = (tasaAnual / 12) / 100 * saldo;
        saldo += interesMensual;
    }

    // Método para aplicar extracto mensual
    public void extractoMensual() {
        saldo -= comisionMensual;
        calcularInteresMensual();
    }

    // Método para imprimir información
    public void imprimir() {
        System.out.println("Saldo: $" + saldo);
        System.out.println("Número de consignaciones: " + numeroConsignaciones);
        System.out.println("Número de retiros: " + numeroRetiros);
        System.out.println("Tasa anual: " + tasaAnual + "%");
        System.out.println("Comisión mensual: $" + comisionMensual);
    }
}

// Clase hija CuentaAhorros
class CuentaAhorros extends Cuenta {
    private boolean activa;

    public CuentaAhorros(float saldoInicial, float tasaAnual) {
        super(saldoInicial, tasaAnual);
        this.activa = saldoInicial >= 10000;
    }

    @Override
    public void consignar(float cantidad) {
        if (activa) {
            super.consignar(cantidad);
        } else {
            System.out.println("Cuenta inactiva, no se puede consignar.");
        }
    }

    @Override
    public void retirar(float cantidad) {
        if (activa) {
            if (cantidad <= saldo) {
                super.retirar(cantidad);
            } else {
                System.out.println("Fondos insuficientes para retirar.");
            }
        } else {
            System.out.println("Cuenta inactiva, no se puede retirar.");
        }
    }

    @Override
    public void extractoMensual() {
        if (numeroRetiros > 4) {
            comisionMensual += (numeroRetiros - 4) * 1000;
        }
        super.extractoMensual();
        activa = saldo >= 10000;
    }

    @Override
    public void imprimir() {
        System.out.println("=== Cuenta de Ahorros ===");
        super.imprimir();
        System.out.println("Cuenta activa: " + activa);
        System.out.println("Total transacciones: " + (numeroConsignaciones + numeroRetiros));
    }
}

// Clase hija CuentaCorriente
class CuentaCorriente extends Cuenta {
    private float sobregiro = 0;

    public CuentaCorriente(float saldoInicial, float tasaAnual) {
        super(saldoInicial, tasaAnual);
    }

    @Override
    public void retirar(float cantidad) {
        if (cantidad <= saldo) {
            saldo -= cantidad;
        } else {
            float exceso = cantidad - saldo;
            saldo = 0;
            sobregiro += exceso;
        }
        numeroRetiros++;
    }

    @Override
    public void consignar(float cantidad) {
        if (sobregiro > 0) {
            if (cantidad >= sobregiro) {
                cantidad -= sobregiro;
                sobregiro = 0;
                saldo += cantidad;
            } else {
                sobregiro -= cantidad;
            }
        } else {
            super.consignar(cantidad);
        }
        numeroConsignaciones++;
    }

    @Override
    public void imprimir() {
        System.out.println("=== Cuenta Corriente ===");
        super.imprimir();
        System.out.println("Total transacciones: " + (numeroConsignaciones + numeroRetiros));
        System.out.println("Sobregiro: $" + sobregiro);
    }
}

// Clase principal con método main
public class Main {
    public static void main(String[] args) {
        // Crear una Cuenta de Ahorros con saldo de $12000 y tasa anual de 12%
        CuentaAhorros cuentaAhorros = new CuentaAhorros(12000, 12);

        // Realizar operaciones
        cuentaAhorros.consignar(3000); // debe funcionar
        cuentaAhorros.retirar(2000);   // debe funcionar
        cuentaAhorros.retirar(1000);
        cuentaAhorros.retirar(500);
        cuentaAhorros.retirar(700);    // 4to retiro
        cuentaAhorros.retirar(600);    // 5to retiro -> genera comisión adicional

        // Aplicar extracto mensual
        cuentaAhorros.extractoMensual();

        // Imprimir estado final
        cuentaAhorros.imprimir();
    }
}
