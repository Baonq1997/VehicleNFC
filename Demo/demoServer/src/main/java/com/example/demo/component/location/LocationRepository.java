package com.example.demo.component.location;

import com.example.demo.component.location.Location;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
//    void deleteByPolicyList(List<Policy> policyList);
//
//    @Query(value = "DELETE FROM tbl_location_has_tbl_policy  WHERE tbl_policy_id = :policyId",
//    nativeQuery = true)
//    @Modifying
//    void deleteLocationPolicyByPolicyId(@Param("policyId") Integer policyId);
//
//    List<Location> findByPolicyList(List<Policy> policyList);
//
//    @Query(value = "INSERT INTO tbl_location_has_tbl_policy(tbl_location_id, tbl_policy_id) VALUES(:locationId,:policyId)",
//    nativeQuery = true)
//    @Modifying
//    void insertLocationAndPolicy(@Param("locationId") Integer locationId, @Param("policyId") Integer policyId);
//
//    Location findByIdAndPolicyList(Integer locationId, List<Policy> policyList);
//
//    @Query(value = "DELETE FROM tbl_location_has_tbl_policy  WHERE tbl_policy_id = :policyId AND tbl_location_id = :locationId ",
//            nativeQuery = true)
//    @Modifying
//    void deleteLocationPolicyByPolicyIdAndLocationId(@Param("policyId") Integer policyId, @Param("locationId") Integer locationId);
//    @Query(value = "SELECT * FROM vehiclenfc.tbl_location_has_tbl_policy where tbl_location_id = :locationId AND tbl_policy_id = :policyId",
//    nativeQuery = true)
//    @Modifying
//    Location findLocation

}
