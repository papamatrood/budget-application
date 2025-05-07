package com.cratechnologie.budget.service.impl;

import com.cratechnologie.budget.service.SupplierService;
import com.cratechnologie.budget.domain.Supplier;
import com.cratechnologie.budget.repository.SupplierRepository;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cratechnologie.budget.domain.Supplier}.
 */
@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierServiceImpl.class);

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Supplier save(Supplier supplier) {
        LOG.debug("Request to save Supplier : {}", supplier);
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier update(Supplier supplier) {
        LOG.debug("Request to update Supplier : {}", supplier);
        return supplierRepository.save(supplier);
    }

    @Override
    public Optional<Supplier> partialUpdate(Supplier supplier) {
        LOG.debug("Request to partially update Supplier : {}", supplier);

        return supplierRepository
            .findById(supplier.getId())
            .map(existingSupplier -> {
                if (supplier.getCompanyName() != null) {
                    existingSupplier.setCompanyName(supplier.getCompanyName());
                }
                if (supplier.getAddress() != null) {
                    existingSupplier.setAddress(supplier.getAddress());
                }
                if (supplier.getPhone() != null) {
                    existingSupplier.setPhone(supplier.getPhone());
                }
                if (supplier.getNifNumber() != null) {
                    existingSupplier.setNifNumber(supplier.getNifNumber());
                }
                if (supplier.getCommercialRegister() != null) {
                    existingSupplier.setCommercialRegister(supplier.getCommercialRegister());
                }
                if (supplier.getBankAccount() != null) {
                    existingSupplier.setBankAccount(supplier.getBankAccount());
                }
                if (supplier.getMandatingEstablishment() != null) {
                    existingSupplier.setMandatingEstablishment(supplier.getMandatingEstablishment());
                }
                if (supplier.getEmail() != null) {
                    existingSupplier.setEmail(supplier.getEmail());
                }
                if (supplier.getWebsite() != null) {
                    existingSupplier.setWebsite(supplier.getWebsite());
                }
                if (supplier.getDescription() != null) {
                    existingSupplier.setDescription(supplier.getDescription());
                }
                if (supplier.getContactFirstname() != null) {
                    existingSupplier.setContactFirstname(supplier.getContactFirstname());
                }
                if (supplier.getContactlastname() != null) {
                    existingSupplier.setContactlastname(supplier.getContactlastname());
                }

                return existingSupplier;
            })
            .map(supplierRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> findOne(Long id) {
        LOG.debug("Request to get Supplier : {}", id);
        return supplierRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Supplier : {}", id);
        supplierRepository.deleteById(id);
    }
}
