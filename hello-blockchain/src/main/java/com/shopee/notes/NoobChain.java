//package com.roaker.notes;
//
//import com.google.common.flogger.LazyArgs;
//import com.google.gson.GsonBuilder;
//import lombok.extern.flogger.Flogger;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Flogger
//public class NoobChain {
//    private static final List<Block> blockChain = new ArrayList<>();
//
//    private static final int difficulty = 5;
//
//
//    public static Boolean isChainValid() {
//        Block currentBlock;
//        Block previousBlock;
//        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
//
//        for (int i = 1; i < blockChain.size(); i++) {
//            currentBlock = blockChain.get(i);
//            previousBlock = blockChain.get(i - 1);
//            if (!StringUtils.equals(currentBlock.getHash(), currentBlock.calculateHash())) {
//                log.atWarning().log("Current Hashes not equal");
//                return false;
//            }
//
//            if (!StringUtils.equals(previousBlock.getHash(), currentBlock.getPreviousHash())) {
//                log.atWarning().log("Previous Hashes not equal");
//                return false;
//            }
//
//            if (!StringUtils.substring(currentBlock.getHash(), 0, difficulty).equals(hashTarget)) {
//                log.atWarning().log("This block hasn't been mined");
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    public static void main(String[] args) {
//        Block genesisBlock = new Block("First Block", "0");
//        log.atInfo().log("Hash for block 1 : %s", LazyArgs.lazy(genesisBlock::getHash));
//
//        Block secondBlock = new Block("Second Block", genesisBlock.getHash());
//        log.atInfo().log("Hash for block 2 : %s", LazyArgs.lazy(secondBlock::getHash));
//
//        Block thirdBlock = new Block("Third Block", secondBlock.getHash());
//        log.atInfo().log("Hash for block 3 : %s", LazyArgs.lazy(thirdBlock::getHash));
//
//        blockChain.add(genesisBlock);
//        blockChain.add(secondBlock);
//        blockChain.add(thirdBlock);
//
//
//        log.atInfo().log("Trying to Mine Block 1....");
//        blockChain.get(0).mineBlock(difficulty);
//        blockChain.get(1).setPreviousHash(blockChain.gzet(0).getHash());
//        log.atInfo().log("Trying to Mine Block 2....");
//        blockChain.get(1).mineBlock(difficulty);
//        blockChain.get(2).setPreviousHash(blockChain.get(1).getHash());
//        log.atInfo().log("Trying to Mine Block 3....");
//        blockChain.get(2).mineBlock(difficulty);
//        log.atInfo().log("Blockchain is Valid: %s", isChainValid());
//
//        String blockChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
//        log.atInfo().log("The block chain: %s", blockChainJson);
//
//    }
//}