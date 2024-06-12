import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductDetailshowComponent } from './product-detailshow.component';

describe('ProductDetailshowComponent', () => {
  let component: ProductDetailshowComponent;
  let fixture: ComponentFixture<ProductDetailshowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductDetailshowComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProductDetailshowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
