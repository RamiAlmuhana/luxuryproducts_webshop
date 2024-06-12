import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteVariantComponent } from './delete-variant.component';

describe('DeleteVariantComponent', () => {
  let component: DeleteVariantComponent;
  let fixture: ComponentFixture<DeleteVariantComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteVariantComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DeleteVariantComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
