
package baitaptuan1;

import java.util.ArrayList;
import java.util.Scanner;

public class QuanLySach {

    static ArrayList<Book> dsSach = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n===== MENU QUAN LY SACH =====");
            System.out.println("1. Them 1 cuon sach");
            System.out.println("2. Xoa 1 cuon sach");
            System.out.println("3. Sua thong tin sach");
            System.out.println("4. Xuat tat ca sach");
            System.out.println("5. Tim sach co ten chua 'lap trinh'");
            System.out.println("6. Lay K sach co gia <= P");
            System.out.println("7. Liet ke sach theo tac gia");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");

            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    themSach();
                    break;
                case 2:
                    xoaSach();
                    break;
                case 3:
                    suaSach();
                    break;
                case 4:
                    xuatSach();
                    break;
                case 5:
                    timSachLapTrinh();
                    break;
                case 6:
                    laySachTheoGia();
                    break;
                case 7:
                    timSachTheoTacGia();
                    break;
                case 0:
                    System.out.println("Ket thuc chuong trinh!");
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 0);
    }

    // 1. Them sach
    static void themSach() {
        System.out.print("Ma sach: ");
        int ma = Integer.parseInt(sc.nextLine());

        System.out.print("Ten sach: ");
        String ten = sc.nextLine();

        System.out.print("Tac gia: ");
        String tg = sc.nextLine();

        System.out.print("Don gia: ");
        double gia = Double.parseDouble(sc.nextLine());

        dsSach.add(new Book(ma, ten, tg, gia));
        System.out.println("Them thanh cong!");
    }

    // 2. Xoa sach
    static void xoaSach() {
        System.out.print("Nhap ma sach can xoa: ");
        int ma = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < dsSach.size(); i++) {
            if (dsSach.get(i).getMaSach() == ma) {
                dsSach.remove(i);
                System.out.println("Da xoa sach!");
                return;
            }
        }
        System.out.println("Khong tim thay sach!");
    }

    // 3. Sua sach
    static void suaSach() {
        System.out.print("Nhap ma sach can sua: ");
        int ma = Integer.parseInt(sc.nextLine());

        for (Book b : dsSach) {
            if (b.getMaSach() == ma) {
                System.out.print("Ten moi: ");
                b.setTenSach(sc.nextLine());

                System.out.print("Tac gia moi: ");
                b.setTacGia(sc.nextLine());

                System.out.print("Gia moi: ");
                b.setDonGia(Double.parseDouble(sc.nextLine()));

                System.out.println("Cap nhat thanh cong!");
                return;
            }
        }
        System.out.println("Khong tim thay sach!");
    }

    // 4. Xuat sach
    static void xuatSach() {
        if (dsSach.isEmpty()) {
            System.out.println("Danh sach rong!");
            return;
        }
        for (Book b : dsSach) {
            System.out.println(b);
        }
    }

    // 5. Tim sach co "lap trinh"
    static void timSachLapTrinh() {
        for (Book b : dsSach) {
            if (b.getTenSach().toLowerCase().contains("lap trinh")) {
                System.out.println(b);
            }
        }
    }

    // 6. Lay K sach gia <= P
    static void laySachTheoGia() {
        System.out.print("Nhap K: ");
        int K = Integer.parseInt(sc.nextLine());

        System.out.print("Nhap P: ");
        double P = Double.parseDouble(sc.nextLine());

        int dem = 0;
        for (Book b : dsSach) {
            if (b.getDonGia() <= P) {
                System.out.println(b);
                dem++;
                if (dem == K)
                    break;
            }
        }
    }

    // 7. Tim sach theo tac gia
    static void timSachTheoTacGia() {
        System.out.print("Nhap so luong tac gia: ");
        int n = Integer.parseInt(sc.nextLine());

        ArrayList<String> dsTacGia = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Tac gia " + (i + 1) + ": ");
            dsTacGia.add(sc.nextLine().toLowerCase());
        }

        for (Book b : dsSach) {
            if (dsTacGia.contains(b.getTacGia().toLowerCase())) {
                System.out.println(b);
            }
        }
    }
}


