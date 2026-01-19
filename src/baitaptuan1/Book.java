
package baitaptuan1;

public class Book {
    private int maSach;
    private String tenSach;
    private String tacGia;
    private double donGia;

    public Book() {
    }

    public Book(int maSach, String tenSach, String tacGia, double donGia) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.donGia = donGia;
    }

    public int getMaSach() {
        return maSach;
    }

    public void setMaSach(int maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    @Override
    public String toString() {
        return "Ma: " + maSach +
               " | Ten: " + tenSach +
               " | Tac gia: " + tacGia +
               " | Gia: " + donGia;
    }
}

