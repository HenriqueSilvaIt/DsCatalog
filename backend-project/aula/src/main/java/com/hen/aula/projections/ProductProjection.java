package com.hen.aula.projections;

public interface ProductProjection extends IdProjection<Long>{

 //   Long getId(); o get Id dele agora vai para o Id projection
    String getName();
}
