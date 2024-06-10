package com.example.gamewebshop.dto.ProductVariantDTOS;

import java.util.List;

// van de order gebruik ik: - maak een nieuwe order aan -> set user de size > zet aantal producten erin
// aantal producten gekocht
// aantal dingen zet je al in de frontend zet dat ook in de dto
// lijst van de cartproduct id`s
//

//name: formData.name,
//infix: formData.infix,
//last_name: formData.lastName,
//zipcode: formData.zipCode,
//houseNumber: formData.houseNumber,
//notes: formData.notes,
//cartProducts: this.products_in_cart,

public class OrderDTO{
    public String name;
    public String infix;
    public String last_name;
    public String zipcode;
    public int houseNumber;
    public String notes;
    public List<Long> cartProductId;




    public OrderDTO(String name, String infix, String last_name, String zipcode, int houseNumber, String notes, long userID) {
        this.name = name;
        this.infix = infix;
        this.last_name = last_name;
        this.zipcode = zipcode;
        this.houseNumber = houseNumber;
        this.notes = notes;
    }
}