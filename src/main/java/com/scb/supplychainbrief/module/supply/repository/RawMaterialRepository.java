package com.scb.supplychainbrief.module.supply.repository;

import com.scb.supplychainbrief.module.supply.model.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {

    @Query("SELECT rm FROM RawMaterial rm WHERE rm.stock < rm.stockMin")
    List<RawMaterial> findLowStockMaterials();
}
