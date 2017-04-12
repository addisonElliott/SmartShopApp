package com.smartshop.smartshopapp;

public class ShoppingListItem
{
    private int id = 0;
    private String name = null;
    private int quantity = 0;
    private boolean selected = false;

    public ShoppingListItem(int id, String name, int quantity)
    {
        super();
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.selected = false;
    }

    public ShoppingListItem(int id, String name, int quantity, boolean selected)
    {
        super();
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.selected = selected;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
}